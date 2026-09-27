package com.fastboot.starter.tenant;

/**
 * 租户上下文：ThreadLocal 保存当前租户 ID，支持 runAs / ignore。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public final class TenantContext {

    private static final ThreadLocal<Long> TENANT = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> IGNORE = ThreadLocal.withInitial(() -> Boolean.FALSE);

    private TenantContext() {}

    public static void set(Long tenantId) {
        TENANT.set(tenantId);
    }

    public static Long get() {
        return TENANT.get();
    }

    public static Long require() {
        Long id = TENANT.get();
        if (id == null) {
            throw new IllegalStateException("TenantContext is empty");
        }
        return id;
    }

    public static void clear() {
        TENANT.remove();
        IGNORE.remove();
    }

    public static boolean isIgnored() {
        return Boolean.TRUE.equals(IGNORE.get());
    }

    public static void setIgnored(boolean ignored) {
        IGNORE.set(ignored);
    }

    public static <T> T runAs(Long tenantId, java.util.function.Supplier<T> action) {
        Long previous = TENANT.get();
        try {
            TENANT.set(tenantId);
            return action.get();
        } finally {
            if (previous == null) {
                TENANT.remove();
            } else {
                TENANT.set(previous);
            }
        }
    }

    public static void runAs(Long tenantId, Runnable action) {
        runAs(
                tenantId,
                () -> {
                    action.run();
                    return null;
                });
    }

    public static <T> T ignore(java.util.function.Supplier<T> action) {
        boolean previous = isIgnored();
        try {
            IGNORE.set(Boolean.TRUE);
            return action.get();
        } finally {
            IGNORE.set(previous);
        }
    }

    public static void ignore(Runnable action) {
        ignore(
                () -> {
                    action.run();
                    return null;
                });
    }
}
