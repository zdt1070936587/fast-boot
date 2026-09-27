package com.fastboot.starter.justauth;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@ConditionalOnProperty(prefix = "fastboot.justauth", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(JustAuthProperties.class)
public class JustAuthAutoConfiguration {}
