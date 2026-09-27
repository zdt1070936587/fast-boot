package com.fastboot.starter.datapermission;

import com.fastboot.modules.authz.api.DataScopePort;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList;
import net.sf.jsqlparser.schema.Column;
import org.springframework.beans.factory.ObjectProvider;

/**
 * 基于组织的数据权限规则。
 *
 * <p>使用 {@link ObjectProvider} 延迟获取 {@link DataScopePort}，避免
 * sqlSessionFactory → interceptor → AuthzAppService → Mapper → sqlSessionFactory 循环依赖。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public class OrgDataPermissionRule implements DataPermissionRule {

    private final ObjectProvider<DataScopePort> dataScopePort;
    private final DataPermissionProperties properties;

    public OrgDataPermissionRule(
            ObjectProvider<DataScopePort> dataScopePort, DataPermissionProperties properties) {
        this.dataScopePort = dataScopePort;
        this.properties = properties;
    }

    @Override
    public Set<String> tables() {
        return new HashSet<>(properties.orgTables());
    }

    @Override
    public Expression where(String tableName, Alias tableAlias, DataPermissionContext context) {
        String columnName = properties.getOrgColumns().get(tableName);
        if (columnName == null) {
            for (var e : properties.getOrgColumns().entrySet()) {
                if (e.getKey().equalsIgnoreCase(tableName)) {
                    columnName = e.getValue();
                    break;
                }
            }
        }
        if (columnName == null) {
            return null;
        }
        ensureVisible(context);
        if (context.isAllVisible()) {
            return null;
        }
        List<Long> ids = context.getVisibleOrgIds();
        if (ids == null || ids.isEmpty()) {
            return new EqualsTo(null, null);
        }
        String aliasName = tableAlias == null ? null : tableAlias.getName();
        Column column =
                new Column(
                        aliasName == null || aliasName.isBlank()
                                ? columnName
                                : aliasName + "." + columnName);
        ExpressionList<Expression> values = new ExpressionList<>();
        for (Long id : ids) {
            values.addExpressions(new LongValue(id));
        }
        return new InExpression(column, new ParenthesedExpressionList<>(values));
    }

    private void ensureVisible(DataPermissionContext context) {
        if (context.getVisibleOrgIds() != null || context.isAllVisible()) {
            return;
        }
        DataScopePort port = dataScopePort.getIfAvailable();
        if (port == null) {
            context.setVisibleOrgIds(List.of());
            return;
        }
        List<Long> visible = port.visibleOrgIds(context.getUserId(), context.getSelfOrgId());
        context.setVisibleOrgIds(visible == null ? List.of() : visible);
    }
}
