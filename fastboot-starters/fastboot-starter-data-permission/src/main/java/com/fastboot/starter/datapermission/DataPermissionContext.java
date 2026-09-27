package com.fastboot.starter.datapermission;

import java.util.List;

/**
 * 请求级数据权限上下文（缓存 visibleOrgIds 等）。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public class DataPermissionContext {

    private static final ThreadLocal<DataPermissionContext> HOLDER = new ThreadLocal<>();

    private final Long userId;
    private final Long selfOrgId;
    private final boolean enabled;
    private List<Long> visibleOrgIds;
    private boolean allVisible;

    public DataPermissionContext(Long userId, Long selfOrgId, boolean enabled) {
        this.userId = userId;
        this.selfOrgId = selfOrgId;
        this.enabled = enabled;
    }

    public static void set(DataPermissionContext ctx) {
        HOLDER.set(ctx);
    }

    public static DataPermissionContext get() {
        return HOLDER.get();
    }

    public static void clear() {
        HOLDER.remove();
    }

    public Long getUserId() {
        return userId;
    }

    public Long getSelfOrgId() {
        return selfOrgId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public List<Long> getVisibleOrgIds() {
        return visibleOrgIds;
    }

    public void setVisibleOrgIds(List<Long> visibleOrgIds) {
        this.visibleOrgIds = visibleOrgIds;
        this.allVisible = visibleOrgIds == null || visibleOrgIds.isEmpty();
    }

    public boolean isAllVisible() {
        return allVisible;
    }
}
