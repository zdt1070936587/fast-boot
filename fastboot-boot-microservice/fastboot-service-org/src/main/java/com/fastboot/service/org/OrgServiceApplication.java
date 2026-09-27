package com.fastboot.service.org;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 组织域微服务启动入口，聚合组织、授权及 DDD 基础设施等模块。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@SpringBootApplication(
        scanBasePackages = {
            "com.fastboot.service.org",
            "com.fastboot.modules.org",
            "com.fastboot.ddd.infrastructure",
            "com.fastboot.ddd.interfaces",
            "com.fastboot.cloud"
        })
public class OrgServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrgServiceApplication.class, args);
    }
}
