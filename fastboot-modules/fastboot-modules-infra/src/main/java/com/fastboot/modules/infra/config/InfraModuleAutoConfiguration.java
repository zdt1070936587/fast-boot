package com.fastboot.modules.infra.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;

/**
 * 基础设施模块自动装配（默认关闭）。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "fastboot.modules.infra", name = "auto-config", havingValue = "true")
@ComponentScan(basePackages = "com.fastboot.modules.infra")
@MapperScan("com.fastboot.modules.infra.infrastructure.mapper")
public class InfraModuleAutoConfiguration {}
