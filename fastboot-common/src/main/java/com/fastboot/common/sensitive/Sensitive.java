package com.fastboot.common.sensitive;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 敏感字段序列化脱敏。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveSerializer.class)
public @interface Sensitive {

    Strategy strategy() default Strategy.DEFAULT;

    enum Strategy {
        DEFAULT,
        MOBILE,
        EMAIL,
        PASSWORD
    }
}
