package com.fastboot.starter.protection;

import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.common.ratelimit.RateLimiter;
import com.fastboot.ddd.infrastructure.aop.SpelSupport;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * {@link RateLimiter} 切面。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Aspect
@Order(100)
public class RateLimiterAspect {

    private final RedissonClient redissonClient;

    public RateLimiterAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Around("@annotation(rateLimiter)")
    public Object around(ProceedingJoinPoint pjp, RateLimiter rateLimiter) throws Throwable {
        String base = SpelSupport.eval(pjp, rateLimiter.key());
        String key = "fb:rl:" + rateLimiter.type().name().toLowerCase() + ":" + base + suffix(rateLimiter);
        RRateLimiter limiter = redissonClient.getRateLimiter(key);
        RateIntervalUnit intervalUnit = toRateIntervalUnit(rateLimiter.unit());
        limiter.trySetRate(RateType.OVERALL, rateLimiter.count(), rateLimiter.period(), intervalUnit);
        if (!limiter.tryAcquire(1)) {
            throw new BizException(ErrorCode.TOO_MANY_REQUESTS);
        }
        return pjp.proceed();
    }

    private static RateIntervalUnit toRateIntervalUnit(java.util.concurrent.TimeUnit unit) {
        return switch (unit) {
            case NANOSECONDS, MICROSECONDS, MILLISECONDS -> RateIntervalUnit.MILLISECONDS;
            case SECONDS -> RateIntervalUnit.SECONDS;
            case MINUTES -> RateIntervalUnit.MINUTES;
            case HOURS -> RateIntervalUnit.HOURS;
            case DAYS -> RateIntervalUnit.DAYS;
        };
    }

    private String suffix(RateLimiter rateLimiter) {
        return switch (rateLimiter.type()) {
            case GLOBAL -> "";
            case IP -> {
                try {
                    ServletRequestAttributes attrs =
                            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                    if (attrs != null) {
                        yield ":" + attrs.getRequest().getRemoteAddr();
                    }
                } catch (Throwable ignored) {
                    // ignore
                }
                yield ":unknown";
            }
            case USER -> {
                try {
                    Object loginId = cn.dev33.satoken.stp.StpUtil.getLoginIdDefaultNull();
                    yield ":" + (loginId == null ? "anon" : loginId);
                } catch (Throwable ignored) {
                    yield ":anon";
                }
            }
        };
    }
}
