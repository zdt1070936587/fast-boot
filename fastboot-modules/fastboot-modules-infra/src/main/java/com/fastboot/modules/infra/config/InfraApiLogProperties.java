package com.fastboot.modules.infra.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * API 访问日志配置。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@ConfigurationProperties(prefix = "fastboot.infra.api-log")
public class InfraApiLogProperties {

    private boolean enabled = false;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
