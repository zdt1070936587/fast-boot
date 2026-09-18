package com.fastboot.boot.monolith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * FastBoot 单体应用启动入口，扫描 {@code com.fastboot} 包下全部组件。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@SpringBootApplication(scanBasePackages = "com.fastboot")
public class FastbootMonolithApplication {
  public static void main(String[] args) {
    SpringApplication.run(FastbootMonolithApplication.class, args);
  }
}
