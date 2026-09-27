package com.fastboot.starter.excel;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Excel starter 自动配置。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@AutoConfiguration
public class ExcelAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ExcelSupport excelSupport() {
        return new ExcelSupport();
    }
}
