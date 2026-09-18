package com.fastboot.modules.user.infrastructure.security;

import com.fastboot.modules.user.domain.PasswordHasher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BcryptPasswordHasher implements PasswordHasher {

  private final PasswordEncoder passwordEncoder;

  public BcryptPasswordHasher(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public boolean matches(String raw, String hash) {
    return passwordEncoder.matches(raw, hash);
  }

  @Override
  public String hash(String raw) {
    return passwordEncoder.encode(raw);
  }
}
