package com.fastboot.modules.infra.infrastructure.web;

import cn.dev33.satoken.stp.StpUtil;
import com.fastboot.common.api.TraceIdHolder;
import com.fastboot.modules.infra.infrastructure.mapper.SysApiAccessLogMapper;
import com.fastboot.modules.infra.infrastructure.persistence.entity.SysApiAccessLogEntity;
import com.fastboot.starter.tenant.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * API 访问日志过滤器。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Component
public class ApiAccessLogFilter extends OncePerRequestFilter {

    private final SysApiAccessLogMapper accessLogMapper;

    public ApiAccessLogFilter(SysApiAccessLogMapper accessLogMapper) {
        this.accessLogMapper = accessLogMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        long start = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
        } finally {
            SysApiAccessLogEntity log = new SysApiAccessLogEntity();
            log.setTenantId(TenantContext.get() != null ? TenantContext.get() : 1L);
            log.setUserId(currentUserId());
            log.setMethod(request.getMethod());
            log.setPath(request.getRequestURI());
            log.setQueryString(request.getQueryString());
            log.setStatus(response.getStatus());
            log.setDurationMs(System.currentTimeMillis() - start);
            log.setTraceId(TraceIdHolder.current());
            log.setClientIp(request.getRemoteAddr());
            accessLogMapper.insert(log);
        }
    }

    private Long currentUserId() {
        try {
            Object loginId = StpUtil.getLoginIdDefaultNull();
            if (loginId instanceof Number number) {
                return number.longValue();
            }
        } catch (Throwable ignored) {
            // ignore
        }
        return null;
    }
}
