package com.fastboot.service.user.client;

import com.fastboot.common.api.R;
import com.fastboot.modules.authz.api.PermissionSnapshot;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "fastboot-authz-service",
    url = "${fastboot.authz.base-url:http://127.0.0.1:18082}",
    contextId = "authzPermissionClient")
public interface AuthzPermissionClient {

  @GetMapping("/api/v1/internal/permissions/{userId}")
  R<PermissionSnapshot> get(@PathVariable("userId") Long userId);
}
