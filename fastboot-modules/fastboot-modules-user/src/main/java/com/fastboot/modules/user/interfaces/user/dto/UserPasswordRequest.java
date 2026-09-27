package com.fastboot.modules.user.interfaces.user.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 重置密码请求。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public record UserPasswordRequest(@NotBlank String password) {}
