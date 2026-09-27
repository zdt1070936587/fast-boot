package com.fastboot.modules.infra.interfaces;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.fastboot.common.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fastboot.modules.infra.application.DataChangeLogQueryService;
import com.fastboot.modules.infra.infrastructure.persistence.entity.SysDataChangeLogEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/infra/data-changes")
@Tag(name = "infra-data-change")
public class DataChangeLogController {

    private final DataChangeLogQueryService dataChangeLogQueryService;

    public DataChangeLogController(DataChangeLogQueryService dataChangeLogQueryService) {
        this.dataChangeLogQueryService = dataChangeLogQueryService;
    }

    @GetMapping
    @SaCheckPermission("sys:audit:read")
    public R<Page<SysDataChangeLogEntity>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return R.ok(dataChangeLogQueryService.page(page, size));
    }
}
