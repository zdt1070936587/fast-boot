package com.fastboot.modules.authz.interfaces.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 创建权限点请求体
 *
 * @author zhaodt
 * @since 2026-09-19
 */
public record PermissionCreateRequest(
        /** 权限类型 */ @NotBlank String permType,
        /** 权限编码 */ @NotBlank String permCode,
        /** 权限显示名称 */ String name) {}
