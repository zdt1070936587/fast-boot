package com.fastboot.service.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(
    scanBasePackages = {
      "com.fastboot.service.user",
      "com.fastboot.modules.user",
      "com.fastboot.modules.authz",
      "com.fastboot.ddd.infrastructure",
      "com.fastboot.ddd.interfaces",
      "com.fastboot.cloud"
    })
@EnableFeignClients(basePackages = "com.fastboot.service.user.client")
public class UserServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(UserServiceApplication.class, args);
  }
}
