package com.fastboot.modules.authz.api;

import java.util.List;

/**
 * 用户权限与角色快照
 *
 * @author zhaodt
 * @since 2026-09-19
 */
public record PermissionSnapshot(
    /** 用户 ID */ Long userId,
    /** 权限编码列表 */ List<String> permissions,
    /** 角色编码列表 */ List<String> roles) {}
