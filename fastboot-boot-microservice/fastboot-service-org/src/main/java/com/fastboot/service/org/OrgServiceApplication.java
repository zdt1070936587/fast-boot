package com.fastboot.service.org;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
    scanBasePackages = {
      "com.fastboot.service.org",
      "com.fastboot.modules.org",
      "com.fastboot.modules.authz",
      "com.fastboot.ddd.infrastructure",
      "com.fastboot.ddd.interfaces",
      "com.fastboot.cloud"
    })
public class OrgServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(OrgServiceApplication.class, args);
  }
}
