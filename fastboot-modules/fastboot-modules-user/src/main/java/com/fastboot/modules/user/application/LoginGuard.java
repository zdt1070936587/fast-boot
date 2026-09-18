package com.fastboot.modules.user.application;

import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import java.time.Duration;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

/**
 * 登录防护：按 IP 限流与按用户名累计失败锁定。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Component
public class LoginGuard {

  /** Redisson 客户端，用于分布式限流与计数 */
  private final RedissonClient redissonClient;

  public LoginGuard(RedissonClient redissonClient) {
    this.redissonClient = redissonClient;
  }

  public void assertAllowed(String username, String clientIp) {
    RRateLimiter limiter = redissonClient.getRateLimiter("fb:login:rl:" + clientIp);
    limiter.trySetRate(RateType.OVERALL, 20, 1, RateIntervalUnit.MINUTES);
    if (!limiter.tryAcquire()) {
      throw new BizException(ErrorCode.LOGIN_RATE_LIMITED);
    }
    RAtomicLong fails = redissonClient.getAtomicLong("fb:login:fail:" + username);
    if (fails.get() >= 5) {
      throw new BizException(ErrorCode.LOGIN_LOCKED);
    }
  }

  public void onFailure(String username) {
    RAtomicLong fails = redissonClient.getAtomicLong("fb:login:fail:" + username);
    fails.incrementAndGet();
    fails.expire(Duration.ofMinutes(15));
  }

  public void onSuccess(String username) {
    redissonClient.getAtomicLong("fb:login:fail:" + username).delete();
  }
}
