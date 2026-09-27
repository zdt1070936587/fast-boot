package com.fastboot.cloud.remote.system;

import com.fastboot.common.api.R;
import com.fastboot.modules.system.api.TenantQueryPort;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 租户查询 Feign 客户端占位（需 system 服务暴露对应内部 API 后启用）。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@FeignClient(
        name = "fastboot-system-service",
        url = "${fastboot.system.base-url:http://127.0.0.1:18084}",
        contextId = "cloudSystemTenantClient")
public interface SystemTenantFeignClient {

    @GetMapping("/api/v1/internal/tenants/{id}")
    R<TenantQueryPort.TenantSnapshot> get(@PathVariable("id") Long id);
}
