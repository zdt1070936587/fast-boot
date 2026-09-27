package com.fastboot.modules.authz.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;

/**
 * 授权模块自动装配（默认关闭，避免与全包扫描重复注册 Bean）。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "fastboot.modules.authz", name = "auto-config", havingValue = "true")
@ComponentScan(basePackages = "com.fastboot.modules.authz")
@MapperScan("com.fastboot.modules.authz.infrastructure.mapper")
public class AuthzModuleAutoConfiguration {}
