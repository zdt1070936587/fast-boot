package com.fastboot.modules.system.interfaces;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fastboot.common.api.R;
import com.fastboot.modules.system.application.TenantPackageAppService;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysTenantPackageEntity;
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
@RequestMapping("/api/v1/system/packages")
@Tag(name = "system-tenant-package")
public class TenantPackageController {

    private final TenantPackageAppService tenantPackageAppService;

    public TenantPackageController(TenantPackageAppService tenantPackageAppService) {
        this.tenantPackageAppService = tenantPackageAppService;
    }

    @GetMapping
    @SaCheckPermission("sys:tenant:read")
    public R<List<SysTenantPackageEntity>> list() {
        return R.ok(tenantPackageAppService.list());
    }

    @GetMapping("/{id}")
    @SaCheckPermission("sys:tenant:read")
    public R<SysTenantPackageEntity> get(@PathVariable Long id) {
        return R.ok(tenantPackageAppService.get(id));
    }

    @PostMapping
    @SaCheckPermission("sys:tenant:write")
    public R<Long> create(@RequestBody SysTenantPackageEntity body) {
        return R.ok(tenantPackageAppService.create(body));
    }

    @PutMapping("/{id}")
    @SaCheckPermission("sys:tenant:write")
    public R<Void> update(@PathVariable Long id, @RequestBody SysTenantPackageEntity body) {
        body.setId(id);
        tenantPackageAppService.update(body);
        return R.ok();
    }
}
