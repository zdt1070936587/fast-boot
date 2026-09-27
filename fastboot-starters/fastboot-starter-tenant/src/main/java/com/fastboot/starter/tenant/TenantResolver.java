package com.fastboot.starter.tenant;

/**
 * 登录前解析租户 ID 的 SPI。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@FunctionalInterface
public interface TenantResolver {

    /**
     * @return 租户 ID，无法解析时返回 null
     */
    Long resolve(jakarta.servlet.http.HttpServletRequest request);
}
