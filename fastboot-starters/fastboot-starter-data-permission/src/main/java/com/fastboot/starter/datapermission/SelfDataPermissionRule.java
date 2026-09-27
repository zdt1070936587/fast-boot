package com.fastboot.starter.datapermission;

import java.util.HashSet;
import java.util.Set;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.schema.Column;

/**
 * 仅本人数据权限规则（created_by = userId）。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public class SelfDataPermissionRule implements DataPermissionRule {

    private final DataPermissionProperties properties;

    public SelfDataPermissionRule(DataPermissionProperties properties) {
        this.properties = properties;
    }

    @Override
    public Set<String> tables() {
        return new HashSet<>(properties.selfTables());
    }

    @Override
    public Expression where(String tableName, Alias tableAlias, DataPermissionContext context) {
        String columnName = properties.getSelfColumns().get(tableName);
        if (columnName == null) {
            for (var e : properties.getSelfColumns().entrySet()) {
                if (e.getKey().equalsIgnoreCase(tableName)) {
                    columnName = e.getValue();
                    break;
                }
            }
        }
        if (columnName == null || context.getUserId() == null) {
            return null;
        }
        String aliasName = tableAlias == null ? null : tableAlias.getName();
        Column column =
                new Column(
                        aliasName == null || aliasName.isBlank()
                                ? columnName
                                : aliasName + "." + columnName);
        return new EqualsTo(column, new LongValue(context.getUserId()));
    }
}
