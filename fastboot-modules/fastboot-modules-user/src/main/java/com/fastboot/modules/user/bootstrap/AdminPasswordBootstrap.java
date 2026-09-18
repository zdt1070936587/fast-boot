package com.fastboot.modules.user.bootstrap;

import com.fastboot.modules.user.domain.PasswordHasher;
import com.fastboot.modules.user.domain.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(20)
@ConditionalOnProperty(name = "fastboot.admin.bootstrap", havingValue = "true")
public class AdminPasswordBootstrap implements ApplicationRunner {

  private static final Logger log = LoggerFactory.getLogger(AdminPasswordBootstrap.class);

  private final UserRepository userRepository;
  private final PasswordHasher passwordHasher;
  private final String adminPassword;

  public AdminPasswordBootstrap(
      UserRepository userRepository,
      PasswordHasher passwordHasher,
      @Value("${fastboot.admin.password:}") String adminPassword) {
    this.userRepository = userRepository;
    this.passwordHasher = passwordHasher;
    this.adminPassword = adminPassword;
  }

  @Override
  public void run(ApplicationArguments args) {
    if (adminPassword == null || adminPassword.isBlank()) {
      throw new IllegalStateException("fastboot.admin.bootstrap=true 时必须设置 fastboot.admin.password");
    }
    userRepository
        .findByUsername("admin")
        .ifPresent(
            user -> {
              if (!user.hasPassword()) {
                userRepository.updatePasswordHash(user.getId(), passwordHasher.hash(adminPassword));
                log.warn("local profile: initialized admin password from configuration");
              }
            });
  }
}
