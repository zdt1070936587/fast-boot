package com.fastboot.modules.system.interfaces;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fastboot.common.api.R;
import com.fastboot.modules.system.application.TenantAppService;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysTenantEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/system/tenants")
@Tag(name = "system-tenant")
public class TenantController {

    private final TenantAppService tenantAppService;

    public TenantController(TenantAppService tenantAppService) {
        this.tenantAppService = tenantAppService;
    }

    @GetMapping
    @SaCheckPermission("sys:tenant:read")
    public R<List<SysTenantEntity>> list() {
        return R.ok(tenantAppService.list());
    }

    @GetMapping("/{id}")
    @SaCheckPermission("sys:tenant:read")
    public R<com.fastboot.modules.system.api.TenantQueryPort.TenantSnapshot> get(@PathVariable Long id) {
        return R.ok(tenantAppService.find(id).orElse(null));
    }

    @PostMapping
    @SaCheckPermission("sys:tenant:write")
    public R<Long> create(@RequestBody SysTenantEntity body) {
        return R.ok(tenantAppService.create(body));
    }

    @PutMapping("/{id}")
    @SaCheckPermission("sys:tenant:write")
    public R<Void> update(@PathVariable Long id, @RequestBody SysTenantEntity body) {
        body.setId(id);
        tenantAppService.update(body);
        return R.ok();
    }
}
