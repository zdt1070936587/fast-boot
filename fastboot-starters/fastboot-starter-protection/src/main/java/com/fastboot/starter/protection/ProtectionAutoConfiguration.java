package com.fastboot.starter.protection;

import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * 防护能力自动配置（限流；锁/幂等仍由 infrastructure 组件扫描提供）。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@AutoConfiguration
public class ProtectionAutoConfiguration {

    @Bean
    @ConditionalOnBean(RedissonClient.class)
    @ConditionalOnMissingBean
    public RateLimiterAspect rateLimiterAspect(RedissonClient redissonClient) {
        return new RateLimiterAspect(redissonClient);
    }
}
