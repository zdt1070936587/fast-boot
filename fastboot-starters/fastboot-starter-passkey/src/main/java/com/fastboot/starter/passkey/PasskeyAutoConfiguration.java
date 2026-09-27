package com.fastboot.starter.passkey;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@ConditionalOnProperty(prefix = "fastboot.passkey", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(PasskeyProperties.class)
public class PasskeyAutoConfiguration {}
