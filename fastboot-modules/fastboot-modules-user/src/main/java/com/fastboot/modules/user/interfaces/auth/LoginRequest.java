package com.fastboot.modules.user.interfaces.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "不能为空") String username,
    @NotBlank(message = "不能为空") String password) {}
