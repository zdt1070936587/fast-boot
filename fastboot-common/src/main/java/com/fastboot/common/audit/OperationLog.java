package com.fastboot.common.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作审计注解，标记需记录业务操作日志的方法。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLog {
    /** 操作名称或业务动作描述 */
    String value();

    /** 要记录的参数 SpEL 表达式，空 = 不记录任何参数（避免密码等敏感字段入日志） */
    String[] args() default {};
}
