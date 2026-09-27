package com.fastboot.starter.tenant;

/**
 * Redis / 缓存 key 的租户前缀工具。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public final class TenantKeyPrefix {

    private TenantKeyPrefix() {}

    public static String of(String key) {
        Long tenantId = TenantContext.get();
        if (tenantId == null) {
            return key;
        }
        return "t:" + tenantId + ":" + key;
    }
}
