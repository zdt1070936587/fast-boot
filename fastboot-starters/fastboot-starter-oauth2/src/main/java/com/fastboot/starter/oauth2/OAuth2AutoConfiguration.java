package com.fastboot.starter.oauth2;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/** OAuth2 客户端自动配置骨架（默认关闭）。 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "fastboot.oauth2", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(OAuth2Properties.class)
public class OAuth2AutoConfiguration {}
