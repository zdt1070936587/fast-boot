package com.fastboot.modules.infra.application.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记需要记录数据变更前后快照的方法。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataChangeLog {

    String aggregateType();

    /** SpEL，解析聚合 ID */
    String aggregateId();
}
