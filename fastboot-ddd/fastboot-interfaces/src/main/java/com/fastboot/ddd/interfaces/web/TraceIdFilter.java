package com.fastboot.ddd.interfaces.web;

import com.fastboot.common.api.TraceIdHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 为每个 HTTP 请求注入或透传链路追踪 ID，并写入响应头。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TraceIdFilter extends OncePerRequestFilter {

  /** 从请求头读取或生成 traceId，绑定 MDC 并在响应结束后清理 */
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String traceId = request.getHeader(TraceIdHolder.HEADER);
    if (traceId == null || traceId.isBlank()) {
      traceId = UUID.randomUUID().toString().replace("-", "");
    }
    TraceIdHolder.set(traceId);
    response.setHeader(TraceIdHolder.HEADER, traceId);
    try {
      filterChain.doFilter(request, response);
    } finally {
      TraceIdHolder.clear();
    }
  }
}
