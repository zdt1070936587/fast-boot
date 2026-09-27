package com.fastboot.modules.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;

/**
 * 用户模块自动装配（默认关闭，避免与 {@code scanBasePackages=com.fastboot} 重复扫描）。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "fastboot.modules.user", name = "auto-config", havingValue = "true")
@ComponentScan(basePackages = "com.fastboot.modules.user")
@MapperScan("com.fastboot.modules.user.infrastructure.mapper")
public class UserModuleAutoConfiguration {}
