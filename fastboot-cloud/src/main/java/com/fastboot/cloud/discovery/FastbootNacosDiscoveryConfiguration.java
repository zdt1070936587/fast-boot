package com.fastboot.cloud.discovery;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(name = "com.alibaba.cloud.nacos.registry.NacosServiceRegistry")
@EnableDiscoveryClient
public class FastbootNacosDiscoveryConfiguration {}
