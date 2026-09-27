package com.fastboot.starter.datapermission;

import java.util.Set;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;

/**
 * 数据权限规则 SPI。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public interface DataPermissionRule {

    /** 适用的表名集合 */
    Set<String> tables();

    /**
     * 返回 WHERE 条件表达式；null 表示不加限制。
     */
    Expression where(String tableName, Alias tableAlias, DataPermissionContext context);
}
