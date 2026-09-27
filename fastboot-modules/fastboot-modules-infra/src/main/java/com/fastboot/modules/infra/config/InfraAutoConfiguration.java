package com.fastboot.modules.infra.config;

import com.fastboot.modules.infra.infrastructure.web.ApiAccessLogFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * Infra 模块自动配置。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Configuration
@EnableConfigurationProperties(InfraApiLogProperties.class)
public class InfraAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "fastboot.infra.api-log", name = "enabled", havingValue = "true")
    public FilterRegistrationBean<ApiAccessLogFilter> apiAccessLogFilter(ApiAccessLogFilter filter) {
        FilterRegistrationBean<ApiAccessLogFilter> bean = new FilterRegistrationBean<>(filter);
        bean.setOrder(Ordered.LOWEST_PRECEDENCE - 10);
        return bean;
    }
}
