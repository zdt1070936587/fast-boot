package com.fastboot.modules.system.interfaces;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fastboot.common.api.R;
import com.fastboot.modules.system.application.ErrorCodeAppService;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysErrorCodeEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/system/error-codes")
@Tag(name = "system-error-code")
public class ErrorCodeController {

    private final ErrorCodeAppService errorCodeAppService;

    public ErrorCodeController(ErrorCodeAppService errorCodeAppService) {
        this.errorCodeAppService = errorCodeAppService;
    }

    @GetMapping
    @SaCheckPermission("sys:error-code:read")
    public R<List<SysErrorCodeEntity>> list() {
        return R.ok(errorCodeAppService.list());
    }

    @GetMapping("/{id}")
    @SaCheckPermission("sys:error-code:read")
    public R<SysErrorCodeEntity> get(@PathVariable Long id) {
        return R.ok(errorCodeAppService.get(id));
    }

    @PostMapping
    @SaCheckPermission("sys:error-code:write")
    public R<Long> create(@RequestBody SysErrorCodeEntity body) {
        return R.ok(errorCodeAppService.create(body));
    }

    @PutMapping("/{id}")
    @SaCheckPermission("sys:error-code:write")
    public R<Void> update(@PathVariable Long id, @RequestBody SysErrorCodeEntity body) {
        body.setId(id);
        errorCodeAppService.update(body);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("sys:error-code:write")
    public R<Void> delete(@PathVariable Long id) {
        errorCodeAppService.delete(id);
        return R.ok();
    }
}
