package com.fastboot.starter.tenant;

import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskDecorator;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 多租户自动配置。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@AutoConfiguration
@EnableConfigurationProperties(TenantProperties.class)
@ConditionalOnProperty(prefix = "fastboot.tenant", name = "enabled", havingValue = "true", matchIfMissing = true)
public class TenantAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(TenantResolver.class)
    public TenantResolver tenantResolver() {
        return new HeaderTenantResolver();
    }

    @Bean
    @ConditionalOnClass(InnerInterceptor.class)
    public FastbootTenantLineInnerInterceptor fastbootTenantLineInnerInterceptor(
            TenantProperties properties) {
        return new FastbootTenantLineInnerInterceptor(properties);
    }

    @Bean
    public TenantIgnoreAspect tenantIgnoreAspect() {
        return new TenantIgnoreAspect();
    }

    @Bean
    public TaskDecorator tenantTaskDecorator() {
        return runnable -> {
            Long tenantId = TenantContext.get();
            boolean ignored = TenantContext.isIgnored();
            return () -> {
                try {
                    if (tenantId != null) {
                        TenantContext.set(tenantId);
                    }
                    TenantContext.setIgnored(ignored);
                    runnable.run();
                } finally {
                    TenantContext.clear();
                }
            };
        };
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public WebMvcConfigurer tenantWebMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new TenantWebInterceptor()).addPathPatterns("/**");
            }
        };
    }
}
