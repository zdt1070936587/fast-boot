package com.fastboot.starter.datapermission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 数据权限配置。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@ConfigurationProperties(prefix = "fastboot.data-permission")
public class DataPermissionProperties {

    /** 是否启用 */
    private boolean enabled = true;
    /** 表名 -> 组织列名 */
    private Map<String, String> orgColumns = defaultOrgColumns();
    /** 表名 -> 创建人列名 */
    private Map<String, String> selfColumns = defaultSelfColumns();

    private static Map<String, String> defaultOrgColumns() {
        Map<String, String> map = new HashMap<>();
        map.put("sys_user", "org_unit_id");
        map.put("sys_org_unit", "id");
        return map;
    }

    private static Map<String, String> defaultSelfColumns() {
        Map<String, String> map = new HashMap<>();
        map.put("sys_operation_log", "created_by");
        return map;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Map<String, String> getOrgColumns() {
        return orgColumns;
    }

    public void setOrgColumns(Map<String, String> orgColumns) {
        this.orgColumns = orgColumns;
    }

    public Map<String, String> getSelfColumns() {
        return selfColumns;
    }

    public void setSelfColumns(Map<String, String> selfColumns) {
        this.selfColumns = selfColumns;
    }

    public List<String> orgTables() {
        return List.copyOf(orgColumns.keySet());
    }

    public List<String> selfTables() {
        return List.copyOf(selfColumns.keySet());
    }
}
