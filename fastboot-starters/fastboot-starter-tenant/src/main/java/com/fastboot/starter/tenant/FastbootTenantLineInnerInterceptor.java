package com.fastboot.starter.tenant;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.springframework.core.annotation.Order;

/**
 * MyBatis-Plus 租户行拦截器（Order=1，最先执行）。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Order(1)
public class FastbootTenantLineInnerInterceptor extends TenantLineInnerInterceptor {

    public FastbootTenantLineInnerInterceptor(TenantProperties properties) {
        super(
                new TenantLineHandler() {
                    @Override
                    public Expression getTenantId() {
                        Long tenantId = TenantContext.get();
                        return new LongValue(tenantId == null ? 0L : tenantId);
                    }

                    @Override
                    public String getTenantIdColumn() {
                        return properties.getColumn();
                    }

                    @Override
                    public boolean ignoreTable(String tableName) {
                        if (TenantContext.isIgnored() || !properties.isEnabled()) {
                            return true;
                        }
                        if (TenantContext.get() == null) {
                            return true;
                        }
                        return properties.getIgnoreTables().stream()
                                .anyMatch(t -> t.equalsIgnoreCase(tableName));
                    }
                });
    }
}
