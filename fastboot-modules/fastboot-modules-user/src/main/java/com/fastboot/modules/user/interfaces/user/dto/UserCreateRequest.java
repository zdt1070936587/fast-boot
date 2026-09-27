package com.fastboot.modules.user.interfaces.user.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 创建用户请求。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public record UserCreateRequest(
        @NotBlank String username,
        @NotBlank String displayName,
        @NotBlank String password,
        Long orgUnitId) {}
