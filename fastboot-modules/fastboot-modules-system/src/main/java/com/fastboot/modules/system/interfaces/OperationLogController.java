package com.fastboot.modules.system.interfaces;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fastboot.common.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fastboot.modules.system.application.OperationLogQueryService;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysOperationLogEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/system/operation-logs")
@Tag(name = "system-operation-log")
public class OperationLogController {

    private final OperationLogQueryService operationLogQueryService;

    public OperationLogController(OperationLogQueryService operationLogQueryService) {
        this.operationLogQueryService = operationLogQueryService;
    }

    @GetMapping
    @SaCheckPermission("sys:audit:read")
    public R<Page<SysOperationLogEntity>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return R.ok(operationLogQueryService.page(page, size));
    }
}
