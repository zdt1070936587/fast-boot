package com.fastboot.modules.authz.domain.event;

/**
 * 用户权限变更领域事件
 *
 * @author zhaodt
 * @since 2026-09-19
 */
public record PermissionsChanged(/** 受影响的用户 ID */ Long userId) {}
