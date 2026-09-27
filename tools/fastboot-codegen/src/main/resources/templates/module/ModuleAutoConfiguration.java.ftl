package com.fastboot.modules.${packageName}.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@ConditionalOnProperty(prefix = "fastboot.modules.${module}", name = "auto-config", havingValue = "true")
@ComponentScan(basePackages = "com.fastboot.modules.${packageName}")
@MapperScan("com.fastboot.modules.${packageName}.infrastructure.mapper")
public class ${pascal}ModuleAutoConfiguration {}
