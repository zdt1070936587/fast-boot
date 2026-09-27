package com.fastboot.modules.system.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;

/**
 * 系统模块自动装配（默认关闭）。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "fastboot.modules.system", name = "auto-config", havingValue = "true")
@ComponentScan(basePackages = "com.fastboot.modules.system")
@MapperScan("com.fastboot.modules.system.infrastructure.mapper")
public class SystemModuleAutoConfiguration {}
