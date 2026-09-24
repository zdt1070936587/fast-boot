package com.fastboot.modules.authz.application.dto;

/**
 * 权限点展示对象
 *
 * @author zhaodt
 * @since 2026-09-19
 */
public record PermissionView(
        /** 权限 ID */ Long id,
        /** 权限类型 */ String permType,
        /** 权限编码 */ String permCode,
        /** 权限名称 */ String name) {}
