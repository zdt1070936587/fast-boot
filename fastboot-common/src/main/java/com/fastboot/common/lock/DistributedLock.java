package com.fastboot.common.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 方法级分布式锁注解，基于 Redisson 保证并发互斥。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {
    /** 锁键表达式，支持 SpEL */
    String key();

    /** 获取锁的最大等待时间，默认 3 */
    long waitTime() default 3;

    /** 锁自动释放的租约时间，默认 10 */
    long leaseTime() default 10;

    /** 等待与租约的时间单位，默认秒 */
    TimeUnit unit() default TimeUnit.SECONDS;
}
