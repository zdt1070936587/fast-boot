package com.fastboot.modules.user.interfaces.auth;

/**
 * 登录成功响应。
 *
 * @param tokenName 令牌在客户端存储/传递时使用的名称
 * @param tokenValue 访问令牌值
 *
 * @author zhaodt
 * @since 2026-09-19
 */
public record LoginResponse(String tokenName, String tokenValue) {}
