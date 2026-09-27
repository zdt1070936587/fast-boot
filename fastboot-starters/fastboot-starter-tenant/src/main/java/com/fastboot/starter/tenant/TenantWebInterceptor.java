package com.fastboot.starter.tenant;

import cn.dev33.satoken.stp.StpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 从 Sa-Token session 恢复租户上下文。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public class TenantWebInterceptor implements HandlerInterceptor {

    public static final String SESSION_TENANT_ID = "tenantId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            if (StpUtil.isLogin()) {
                Object tenantId = StpUtil.getSession().get(SESSION_TENANT_ID);
                if (tenantId instanceof Number n) {
                    TenantContext.set(n.longValue());
                }
            }
        } catch (Throwable ignored) {
            // Sa-Token 未初始化时忽略
        }
        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        TenantContext.clear();
    }
}
