package com.fastboot.starter.tenant;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 默认从请求头 {@code X-Tenant-Id} 解析租户。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public class HeaderTenantResolver implements TenantResolver {

    public static final String HEADER = "X-Tenant-Id";

    @Override
    public Long resolve(HttpServletRequest request) {
        String value = request.getHeader(HEADER);
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
