package com.fastboot.starter.datapermission;

import com.fastboot.modules.authz.api.DataScopePort;
import java.util.List;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 数据权限自动配置。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@AutoConfiguration
@EnableConfigurationProperties(DataPermissionProperties.class)
@ConditionalOnProperty(
        prefix = "fastboot.data-permission",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true)
public class DataPermissionAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OrgDataPermissionRule orgDataPermissionRule(
            ObjectProvider<DataScopePort> dataScopePort, DataPermissionProperties properties) {
        return new OrgDataPermissionRule(dataScopePort, properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public SelfDataPermissionRule selfDataPermissionRule(DataPermissionProperties properties) {
        return new SelfDataPermissionRule(properties);
    }

    @Bean
    @ConditionalOnClass(name = "com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor")
    public FastbootDataPermissionInterceptor fastbootDataPermissionInterceptor(
            ObjectProvider<DataPermissionRule> rules) {
        // 延迟解析规则，避免在 sqlSessionFactory 创建阶段拉起业务 Bean
        return new FastbootDataPermissionInterceptor(() -> rules.orderedStream().toList());
    }

    @Bean
    public DataPermissionAspect dataPermissionAspect(DataPermissionProperties properties) {
        return new DataPermissionAspect(properties);
    }
}
