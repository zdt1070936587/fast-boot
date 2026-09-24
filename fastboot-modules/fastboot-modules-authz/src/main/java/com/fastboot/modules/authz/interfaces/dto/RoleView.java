package com.fastboot.modules.authz.interfaces.dto;

/**
 * 角色展示对象
 *
 * @author zhaodt
 * @since 2026-09-19
 */
public record RoleView(
        /** 角色 ID */ Long id, /** 角色编码 */ String code, /** 角色名称 */ String name) {}
