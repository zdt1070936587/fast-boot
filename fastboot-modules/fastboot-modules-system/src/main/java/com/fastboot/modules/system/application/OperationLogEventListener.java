package com.fastboot.modules.system.application;

import com.fastboot.common.audit.OperationLogEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 操作日志事件监听器。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Component
public class OperationLogEventListener {

    private final AuditAppService auditAppService;

    public OperationLogEventListener(AuditAppService auditAppService) {
        this.auditAppService = auditAppService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onOperationLog(OperationLogEvent event) {
        auditAppService.record(event);
    }
}
