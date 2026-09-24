package com.fastboot.modules.authz.interfaces.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 创建角色请求体
 *
 * @author zhaodt
 * @since 2026-09-19
 */
public record RoleCreateRequest(
        /** 角色编码 */ @NotBlank String code, /** 角色名称 */ @NotBlank String name) {}
