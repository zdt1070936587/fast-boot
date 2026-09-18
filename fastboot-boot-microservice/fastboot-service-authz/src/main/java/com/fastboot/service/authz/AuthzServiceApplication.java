package com.fastboot.service.authz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 授权域微服务启动入口，提供权限与内部 API 能力。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@SpringBootApplication(
    scanBasePackages = {
      "com.fastboot.service.authz",
      "com.fastboot.modules.authz",
      "com.fastboot.ddd.infrastructure",
      "com.fastboot.ddd.interfaces",
      "com.fastboot.cloud"
    })
public class AuthzServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(AuthzServiceApplication.class, args);
  }
}
