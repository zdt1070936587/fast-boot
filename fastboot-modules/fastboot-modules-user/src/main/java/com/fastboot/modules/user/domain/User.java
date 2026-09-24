package com.fastboot.modules.user.domain;

import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.ddd.domain.model.AggregateRoot;

/**
 * 用户聚合根。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
public class User extends AggregateRoot<Long> {
    /** 登录用户名 */
    private String username;
    /** 显示名称 */
    private String displayName;
    /** 密码哈希值 */
    private String passwordHash;
    /** 是否启用 */
    private boolean enabled;
    /** 所属组织单元 ID */
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
