package com.fastboot.modules.system.api;

import com.fastboot.common.audit.OperationLogEvent;

/**
 * 审计落库端口。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public interface AuditPort {

    void record(OperationLogEvent event);

    void recordLogin(
            Long tenantId,
            Long userId,
            String username,
            String eventType,
            String clientIp,
            String userAgent,
            boolean success,
            String message);
}
