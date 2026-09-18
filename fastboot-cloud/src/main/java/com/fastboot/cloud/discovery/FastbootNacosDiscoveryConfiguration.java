package com.fastboot.cloud.discovery;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;

/**
 * Nacos 服务发现自动配置，在存在 Nacos 注册实现时启用 {@link EnableDiscoveryClient}。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(name = "com.alibaba.cloud.nacos.registry.NacosServiceRegistry")
@EnableDiscoveryClient
public class FastbootNacosDiscoveryConfiguration {}
