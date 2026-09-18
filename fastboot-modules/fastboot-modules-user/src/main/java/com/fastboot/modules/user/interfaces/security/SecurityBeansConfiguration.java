package com.fastboot.modules.user.interfaces.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 用户模块安全相关 Spring Bean 配置。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Configuration
public class SecurityBeansConfiguration {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
