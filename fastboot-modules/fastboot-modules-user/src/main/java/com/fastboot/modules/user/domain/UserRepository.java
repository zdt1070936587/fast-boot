package com.fastboot.modules.user.domain;

import java.util.Optional;

public interface UserRepository {
  Optional<User> findByUsername(String username);

  Optional<User> findById(Long id);

  void updatePasswordHash(Long id, String passwordHash);

  void updateOrgUnit(Long id, Long orgUnitId);
}
