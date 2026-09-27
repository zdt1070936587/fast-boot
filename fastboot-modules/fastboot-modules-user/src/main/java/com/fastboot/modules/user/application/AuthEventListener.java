package com.fastboot.modules.user.application;

import cn.dev33.satoken.stp.StpUtil;
import com.fastboot.modules.system.api.AuditPort;
import com.fastboot.modules.user.domain.event.UserLoggedIn;
import com.fastboot.modules.user.domain.event.UserLoggedOut;
import com.fastboot.starter.tenant.TenantContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 认证领域事件监听：登录审计落库（经 {@link AuditPort}）。
 *
 * @author zhaodt
 * @since 2026-09-24
 */
@Component
public class AuthEventListener {

    private static final Logger log = LoggerFactory.getLogger(AuthEventListener.class);

    private final ObjectProvider<AuditPort> auditPort;

    public AuthEventListener(ObjectProvider<AuditPort> auditPort) {
        this.auditPort = auditPort;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onLoggedIn(UserLoggedIn event) {
        log.info("audit event=UserLoggedIn userId={} username={}", event.userId(), event.username());
        AuditPort port = auditPort.getIfAvailable();
        if (port == null) {
            return;
        }
        port.recordLogin(
                currentTenantId(), event.userId(), event.username(), "LOGIN", null, null, true, null);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onLoggedOut(UserLoggedOut event) {
        log.info("audit event=UserLoggedOut userId={}", event.userId());
        AuditPort port = auditPort.getIfAvailable();
        if (port == null) {
            return;
        }
        port.recordLogin(currentTenantId(), event.userId(), null, "LOGOUT", null, null, true, null);
    }

    private Long currentTenantId() {
        Long tenantId = TenantContext.get();
        if (tenantId != null) {
            return tenantId;
        }
        try {
            Object raw = StpUtil.getSession().get("tenantId");
            if (raw instanceof Number number) {
                return number.longValue();
            }
        } catch (Throwable ignored) {
            // ignore
        }
        return 1L;
    }
}
