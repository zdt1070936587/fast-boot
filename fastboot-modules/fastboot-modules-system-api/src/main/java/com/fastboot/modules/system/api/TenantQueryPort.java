package com.fastboot.modules.system.api;

import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * 租户查询端口。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public interface TenantQueryPort {

    Optional<TenantSnapshot> find(Long id);

    Optional<TenantSnapshot> findByCode(String code);

    record TenantSnapshot(
            Long id,
            String code,
            String name,
            Long packageId,
            String status,
            OffsetDateTime expireAt) {

        public boolean active() {
            if (!"ENABLED".equalsIgnoreCase(status)) {
                return false;
            }
            return expireAt == null || expireAt.isAfter(OffsetDateTime.now());
        }
    }
}
