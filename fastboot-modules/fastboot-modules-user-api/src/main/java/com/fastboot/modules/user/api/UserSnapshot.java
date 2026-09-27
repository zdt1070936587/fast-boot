package com.fastboot.modules.user.api;

/**
 * 用户快照。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public record UserSnapshot(
        Long id, String username, String displayName, boolean enabled, Long orgUnitId, Long tenantId) {}
