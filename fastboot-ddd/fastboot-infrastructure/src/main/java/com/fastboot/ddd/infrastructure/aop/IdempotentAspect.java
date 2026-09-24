package com.fastboot.ddd.infrastructure.aop;

import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.common.idempotent.Idempotent;
import java.time.Duration;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * {@link Idempotent} 注解切面，使用 Redis 桶实现短时幂等。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Aspect
@Component
@Order(100)
public class IdempotentAspect {

    /** Redisson 客户端 */
    private final RedissonClient redissonClient;

    public IdempotentAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /** 首次请求写入幂等键并执行业务，重复请求抛出幂等冲突；业务异常时释放键以便重试 */
    @Around("@annotation(idempotent)")
    public Object around(ProceedingJoinPoint pjp, Idempotent idempotent) throws Throwable {
        String key = "fb:idem:" + SpelSupport.eval(pjp, idempotent.key());
        RBucket<Boolean> bucket = redissonClient.getBucket(key);
        boolean first =
                bucket.setIfAbsent(
                        Boolean.TRUE, Duration.of(idempotent.ttl(), idempotent.unit().toChronoUnit()));
        if (!first) {
            throw new BizException(ErrorCode.IDEMPOTENT_CONFLICT);
        }
        try {
            return pjp.proceed();
        } catch (Throwable ex) {
            bucket.delete();
            throw ex;
        }
    }
}
