package com.fastboot.modules.user.interfaces.auth;

import jakarta.validation.constraints.NotBlank;

/**
 * 登录请求体。
 *
 * @param username 用户名
 * @param password 密码
 *
 * @author zhaodt
 * @since 2026-09-19
 */
public record LoginRequest(
        @NotBlank(message = "不能为空") String username,
        @NotBlank(message = "不能为空") String password) {}
