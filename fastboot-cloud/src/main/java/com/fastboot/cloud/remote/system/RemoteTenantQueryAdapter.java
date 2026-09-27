package com.fastboot.cloud.remote.system;

import com.fastboot.modules.system.api.TenantQueryPort;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 租户远程查询占位实现；默认未启用 Feign 调用，返回空。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Component
@ConditionalOnClass(name = "org.springframework.cloud.openfeign.FeignClient")
@ConditionalOnProperty(prefix = "fastboot.remote.tenant", name = "enabled", havingValue = "true")
public class RemoteTenantQueryAdapter implements TenantQueryPort {

    private final SystemTenantFeignClient client;

    public RemoteTenantQueryAdapter(SystemTenantFeignClient client) {
        this.client = client;
    }

    @Override
    public Optional<TenantSnapshot> find(Long id) {
        // TODO: 完善错误处理与缓存
        var response = client.get(id);
        if (response == null || response.getData() == null) {
            return Optional.empty();
        }
        return Optional.of(response.getData());
    }

    @Override
    public Optional<TenantSnapshot> findByCode(String code) {
        // TODO: 按租户编码查询的 Feign 端点
        return Optional.empty();
    }
}
