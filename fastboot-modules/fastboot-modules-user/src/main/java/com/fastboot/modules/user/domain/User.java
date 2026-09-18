package com.fastboot.modules.user.domain;

import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.ddd.domain.model.AggregateRoot;

public class User extends AggregateRoot<Long> {
  private String username;
  private String displayName;
  private String passwordHash;
  private boolean enabled;
  private Long orgUnitId;

  public static User restore(
      Long id,
      String username,
      String displayName,
      String passwordHash,
      boolean enabled,
      Long orgUnitId,
      Long version) {
    User user = new User();
    user.setId(id);
    user.username = username;
    user.displayName = displayName;
    user.passwordHash = passwordHash;
    user.enabled = enabled;
    user.orgUnitId = orgUnitId;
    user.setVersion(version);
    return user;
  }

  public void assertCanLogin(String rawPassword, PasswordHasher hasher) {
    if (!enabled) {
      throw new BizException(ErrorCode.ACCOUNT_DISABLED);
    }
    if (passwordHash == null || passwordHash.isBlank()) {
      throw new BizException(ErrorCode.PASSWORD_NOT_INITIALIZED);
    }
    if (!hasher.matches(rawPassword, passwordHash)) {
      throw new BizException(ErrorCode.USER_OR_PASSWORD);
    }
  }

  public String getUsername() {
    return username;
  }

  public String getDisplayName() {
    return displayName;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public boolean hasPassword() {
    return passwordHash != null && !passwordHash.isBlank();
  }

  public Long getOrgUnitId() {
    return orgUnitId;
  }

  public void assignOrg(Long orgUnitId) {
    this.orgUnitId = orgUnitId;
  }
}
