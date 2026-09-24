package com.fastboot.ddd.infrastructure.aop;

import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.common.lock.DistributedLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * {@link DistributedLock} 注解切面，基于 Redisson 实现方法级分布式锁。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Aspect
@Component
@Order(100)
public class DistributedLockAspect {

    /** Redisson 客户端 */
    private final RedissonClient redissonClient;

    public DistributedLockAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /** 加锁执行目标方法，失败时抛出业务冲突异常 */
    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint pjp, DistributedLock distributedLock) throws Throwable {
        String key = "fb:lock:" + SpelSupport.eval(pjp, distributedLock.key());
        RLock lock = redissonClient.getLock(key);
        boolean locked =
                lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.unit());
        if (!locked) {
            throw new BizException(ErrorCode.CONFLICT, "获取分布式锁失败");
        }
        try {
            return pjp.proceed();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
