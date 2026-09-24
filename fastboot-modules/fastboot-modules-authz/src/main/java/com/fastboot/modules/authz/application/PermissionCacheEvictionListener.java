package com.fastboot.modules.authz.application;

import com.fastboot.modules.authz.api.PermissionQueryPort;
import com.fastboot.modules.authz.domain.event.PermissionsChanged;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 权限变更提交后失效 Redis 缓存，避免事务内 evict 导致脏回填。
 *
 * @author zhaodt
 * @since 2026-09-24
 */
@Component
public class PermissionCacheEvictionListener {

    private final PermissionQueryPort permissionQueryPort;

    public PermissionCacheEvictionListener(PermissionQueryPort permissionQueryPort) {
        this.permissionQueryPort = permissionQueryPort;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void on(PermissionsChanged event) {
        permissionQueryPort.evict(event.userId());
    }
}
