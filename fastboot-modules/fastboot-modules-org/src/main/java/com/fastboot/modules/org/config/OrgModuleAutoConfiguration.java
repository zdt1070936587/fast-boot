package com.fastboot.modules.org.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;

/**
 * 组织模块自动装配（默认关闭）。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "fastboot.modules.org", name = "auto-config", havingValue = "true")
@ComponentScan(basePackages = "com.fastboot.modules.org")
@MapperScan("com.fastboot.modules.org.infrastructure.mapper")
public class OrgModuleAutoConfiguration {}
