package com.fastboot.modules.user.interfaces.user.dto;

/**
 * 更新用户请求。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public record UserUpdateRequest(String displayName, Boolean enabled, Long orgUnitId) {}
