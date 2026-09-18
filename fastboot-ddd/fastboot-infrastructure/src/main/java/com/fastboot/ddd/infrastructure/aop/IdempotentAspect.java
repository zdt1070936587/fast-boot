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
import org.springframework.stereotype.Component;

@Aspect
@Component
public class IdempotentAspect {

  private final RedissonClient redissonClient;

  public IdempotentAspect(RedissonClient redissonClient) {
    this.redissonClient = redissonClient;
  }

  @Around("@annotation(idempotent)")
  public Object around(ProceedingJoinPoint pjp, Idempotent idempotent) throws Throwable {
    String key = "fb:idem:" + DistributedLockAspect.eval(pjp, idempotent.key());
    RBucket<Boolean> bucket = redissonClient.getBucket(key);
    boolean first = bucket.setIfAbsent(Boolean.TRUE, Duration.of(idempotent.ttl(), idempotent.unit().toChronoUnit()));
    if (!first) {
      throw new BizException(ErrorCode.IDEMPOTENT_CONFLICT);
    }
    return pjp.proceed();
  }
}
