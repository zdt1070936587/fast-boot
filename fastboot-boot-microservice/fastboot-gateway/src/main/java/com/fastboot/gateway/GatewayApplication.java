package com.fastboot.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * API 网关服务启动入口，负责路由转发与全局过滤器链。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@SpringBootApplication(scanBasePackages = "com.fastboot.gateway")
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
