package com.fastboot.common.ratelimit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 方法级限流注解。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimiter {

    /** 限流键 SpEL */
    String key();

    /** 时间窗口内允许次数 */
    long count() default 100;

    /** 时间窗口长度 */
    long period() default 60;

    /** 时间单位 */
    TimeUnit unit() default TimeUnit.SECONDS;

    /** 限流维度 */
    Type type() default Type.GLOBAL;

    enum Type {
        GLOBAL,
        IP,
        USER
    }
}
