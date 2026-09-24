package com.fastboot.modules.user.infrastructure.security;

import com.fastboot.modules.user.domain.PasswordHasher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 基于 BCrypt 的 {@link PasswordHasher} 实现。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Component
public class BcryptPasswordHasher implements PasswordHasher {

    /** Spring Security 密码编码器 */
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
