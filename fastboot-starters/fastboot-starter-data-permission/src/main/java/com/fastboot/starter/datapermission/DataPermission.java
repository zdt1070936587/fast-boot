package com.fastboot.starter.datapermission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限开关（类/方法级）。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataPermission {

    /** 是否启用数据权限，默认 true */
    boolean enabled() default true;
}
