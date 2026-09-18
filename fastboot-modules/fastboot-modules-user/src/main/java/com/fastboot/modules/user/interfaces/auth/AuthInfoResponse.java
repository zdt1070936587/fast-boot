package com.fastboot.modules.user.interfaces.auth;

/**
 * 当前登录用户基本信息响应。
 *
 * @param userId 用户 ID
 * @param username 用户名
 * @param displayName 显示名称
 * @param orgUnitId 所属组织单元 ID
 *
 * @author zhaodt
 * @since 2026-09-19
 */
public record AuthInfoResponse(Long userId, String username, String displayName, Long orgUnitId) {}
