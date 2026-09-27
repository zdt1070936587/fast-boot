package com.fastboot.modules.system.application;

import com.fastboot.common.audit.OperationLogEvent;
import com.fastboot.modules.system.api.AuditPort;
import com.fastboot.modules.system.infrastructure.mapper.SysLoginLogMapper;
import com.fastboot.modules.system.infrastructure.mapper.SysOperationLogMapper;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysLoginLogEntity;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysOperationLogEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 审计落库应用服务。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Service
public class AuditAppService implements AuditPort {

    private final SysOperationLogMapper operationLogMapper;
    private final SysLoginLogMapper loginLogMapper;

    public AuditAppService(SysOperationLogMapper operationLogMapper, SysLoginLogMapper loginLogMapper) {
        this.operationLogMapper = operationLogMapper;
        this.loginLogMapper = loginLogMapper;
    }

    @Override
    @Transactional
    public void record(OperationLogEvent event) {
        SysOperationLogEntity entity = new SysOperationLogEntity();
        entity.setTenantId(event.tenantId() != null ? event.tenantId() : 1L);
        entity.setUserId(event.userId());
        entity.setAction(event.action());
        entity.setMethod(event.method());
        entity.setArgs(event.args());
        entity.setResult(event.result());
        entity.setDurationMs(event.durationMs());
        entity.setTraceId(event.traceId());
        entity.setClientIp(event.clientIp());
        entity.setUserAgent(event.userAgent());
        entity.setSuccess(event.success());
        entity.setErrorCode(event.errorCode());
        entity.setCreatedBy(event.userId());
        operationLogMapper.insert(entity);
    }

    @Override
    @Transactional
    public void recordLogin(
            Long tenantId,
            Long userId,
            String username,
            String eventType,
            String clientIp,
            String userAgent,
            boolean success,
            String message) {
        SysLoginLogEntity entity = new SysLoginLogEntity();
        entity.setTenantId(tenantId != null ? tenantId : 1L);
        entity.setUserId(userId);
        entity.setUsername(username);
        entity.setEventType(eventType);
        entity.setClientIp(clientIp);
        entity.setUserAgent(userAgent);
        entity.setSuccess(success);
        entity.setMessage(message);
        loginLogMapper.insert(entity);
    }
}
