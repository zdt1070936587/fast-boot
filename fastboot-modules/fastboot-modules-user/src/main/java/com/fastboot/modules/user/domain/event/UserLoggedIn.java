package com.fastboot.modules.user.domain.event;

/**
 * 用户登录成功领域事件。
 *
 * @param userId 用户 ID
 * @param username 用户名
 *
 * @author zhaodt
 * @since 2026-09-19
 */
public record UserLoggedIn(Long userId, String username) {}
