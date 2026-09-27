package com.fastboot.common.audit;

/**
 * 操作审计领域事件（由切面发布，system 模块落库）。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public record OperationLogEvent(
        Long tenantId,
        Long userId,
        String action,
        String method,
        String args,
        String result,
        long durationMs,
        String traceId,
        String clientIp,
        String userAgent,
        boolean success,
        Integer errorCode) {}
