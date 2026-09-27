package com.fastboot.modules.system.application.dto;

/**
 * 在线用户视图。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public record OnlineUserView(Long userId, Long tenantId, String token) {}
