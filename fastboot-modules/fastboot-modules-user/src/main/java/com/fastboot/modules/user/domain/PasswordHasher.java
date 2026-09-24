package com.fastboot.modules.user.domain;

/**
 * 密码哈希与校验端口（领域层）。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
public interface PasswordHasher {
    boolean matches(String raw, String hash);

    String hash(String raw);
}
