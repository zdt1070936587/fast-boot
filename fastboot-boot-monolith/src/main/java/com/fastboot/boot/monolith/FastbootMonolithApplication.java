package com.fastboot.boot.monolith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.fastboot")
public class FastbootMonolithApplication {
  public static void main(String[] args) {
    SpringApplication.run(FastbootMonolithApplication.class, args);
  }
}
