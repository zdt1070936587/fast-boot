package com.fastboot.modules.user.application.dto;

/**
 * 用户列表/详情视图。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public record UserView(Long id, String username, String displayName, boolean enabled, Long orgUnitId) {}
