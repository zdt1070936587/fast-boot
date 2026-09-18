package com.fastboot.modules.user.domain.event;

/**
 * 用户已登出领域事件。
 *
 * @param userId 用户 ID
 *
 * @author zhaodt
 * @since 2026-09-19
 */
public record UserLoggedOut(Long userId) {}
