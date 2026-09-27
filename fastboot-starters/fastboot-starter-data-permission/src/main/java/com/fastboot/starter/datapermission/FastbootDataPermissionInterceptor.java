package com.fastboot.starter.datapermission;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.schema.Table;
import org.springframework.core.annotation.Order;

/**
 * 数据权限拦截器（Order=2）。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Order(2)
public class FastbootDataPermissionInterceptor extends DataPermissionInterceptor {

    public FastbootDataPermissionInterceptor(Supplier<List<DataPermissionRule>> rulesSupplier) {
        super(
                new MultiDataPermissionHandler() {
                    @Override
                    public Expression getSqlSegment(
                            Table table, Expression where, String mappedStatementId) {
                        DataPermissionContext ctx = DataPermissionContext.get();
                        if (ctx == null || !ctx.isEnabled()) {
                            return null;
                        }
                        List<DataPermissionRule> rules = rulesSupplier.get();
                        if (rules == null || rules.isEmpty()) {
                            return null;
                        }
                        String tableName = table.getName().replace("\"", "");
                        Alias alias = table.getAlias();
                        List<Expression> expressions = new ArrayList<>();
                        for (DataPermissionRule rule : rules) {
                            if (rule.tables().stream().anyMatch(t -> t.equalsIgnoreCase(tableName))) {
                                Expression expr = rule.where(tableName, alias, ctx);
                                if (expr != null) {
                                    expressions.add(expr);
                                }
                            }
                        }
                        if (expressions.isEmpty()) {
                            return null;
                        }
                        Expression result = expressions.get(0);
                        for (int i = 1; i < expressions.size(); i++) {
                            result = new AndExpression(result, expressions.get(i));
                        }
                        return result;
                    }
                });
    }

    public static Set<String> collectTables(List<DataPermissionRule> rules) {
        return rules.stream().flatMap(r -> r.tables().stream()).collect(Collectors.toSet());
    }

    public static String column(String tableAlias, String column) {
        if (tableAlias == null || tableAlias.isBlank()) {
            return column;
        }
        return tableAlias + StringPool.DOT + column;
    }
}
