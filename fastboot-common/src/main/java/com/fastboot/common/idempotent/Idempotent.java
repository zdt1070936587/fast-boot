package com.fastboot.common.idempotent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 方法级幂等控制注解，基于 Redis 在 TTL 内拒绝重复请求。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {
    /** 幂等键表达式，支持 SpEL（如 {@code #orderId}） */
    String key();

    /** 幂等记录存活时间，默认 10 */
    long ttl() default 10;

    /** {@link #ttl()} 的时间单位，默认秒 */
    TimeUnit unit() default TimeUnit.SECONDS;
}
