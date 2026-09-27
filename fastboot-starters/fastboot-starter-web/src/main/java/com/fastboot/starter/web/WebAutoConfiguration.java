package com.fastboot.starter.web;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Web 层自动配置：统一 i18n {@link MessageSource}（键前缀 {@code error.*}）。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@AutoConfiguration
public class WebAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "fastbootMessageSource")
    public MessageSource fastbootMessageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("messages");
        source.setDefaultEncoding("UTF-8");
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }
}
