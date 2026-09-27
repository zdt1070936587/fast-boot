package com.fastboot.starter.tenant;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 多租户配置。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@ConfigurationProperties(prefix = "fastboot.tenant")
public class TenantProperties {

    /** 是否启用多租户 */
    private boolean enabled = true;
    /** 是否启用 PostgreSQL RLS 兜底（默认关闭） */
    private boolean rls = false;
    /** 忽略租户条件的表名 */
    private List<String> ignoreTables = defaultIgnoreTables();
    /** 列名 */
    private String column = "tenant_id";

    private static List<String> defaultIgnoreTables() {
        List<String> tables = new ArrayList<>();
        tables.add("sys_tenant");
        tables.add("sys_tenant_package");
        tables.add("flyway_schema_history");
        return tables;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isRls() {
        return rls;
    }

    public void setRls(boolean rls) {
        this.rls = rls;
    }

    public List<String> getIgnoreTables() {
        return ignoreTables;
    }

    public void setIgnoreTables(List<String> ignoreTables) {
        this.ignoreTables = ignoreTables;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }
}
