package com.fastboot.starter.captcha;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@ConditionalOnProperty(prefix = "fastboot.captcha", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(CaptchaProperties.class)
public class CaptchaAutoConfiguration {}
