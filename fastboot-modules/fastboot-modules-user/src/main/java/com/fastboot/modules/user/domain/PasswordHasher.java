package com.fastboot.modules.user.domain;

public interface PasswordHasher {
  boolean matches(String raw, String hash);

  String hash(String raw);
}
