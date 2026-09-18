package com.fastboot.service.authz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
