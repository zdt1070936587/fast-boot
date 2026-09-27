package com.fastboot.modules.user.domain;

import java.util.List;
import java.util.Optional;

/**
 * 用户仓储端口（领域层）。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
public interface UserRepository {
    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    User save(User user);

    List<User> findAll();

    void deleteById(Long id);
}
