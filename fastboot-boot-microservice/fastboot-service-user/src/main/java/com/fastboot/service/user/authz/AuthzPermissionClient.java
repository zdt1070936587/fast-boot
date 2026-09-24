package com.fastboot.service.user.authz;

import com.fastboot.common.api.R;
import com.fastboot.modules.authz.api.DataScopeSnapshot;
import com.fastboot.modules.authz.api.PermissionSnapshot;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 授权服务 Feign 客户端。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@FeignClient(
        name = "fastboot-authz-service",
        url = "${fastboot.authz.base-url:http://127.0.0.1:18082}",
        contextId = "authzPermissionClient")
public interface AuthzPermissionClient {

    @GetMapping("/api/v1/internal/permissions/{userId}")
    R<PermissionSnapshot> get(@PathVariable("userId") Long userId);

    @GetMapping("/api/v1/internal/data-scope/{userId}")
    R<DataScopeSnapshot> dataScope(
            @PathVariable("userId") Long userId, @RequestParam(value = "selfOrgId", required = false) Long selfOrgId);

    @DeleteMapping("/api/v1/internal/permissions/{userId}/cache")
    R<Void> evictCache(@PathVariable("userId") Long userId);
}
