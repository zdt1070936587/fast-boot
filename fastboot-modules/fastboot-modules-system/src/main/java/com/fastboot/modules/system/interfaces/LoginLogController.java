package com.fastboot.modules.system.interfaces;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fastboot.common.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fastboot.modules.system.application.LoginLogQueryService;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysLoginLogEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/system/login-logs")
@Tag(name = "system-login-log")
public class LoginLogController {

    private final LoginLogQueryService loginLogQueryService;

    public LoginLogController(LoginLogQueryService loginLogQueryService) {
        this.loginLogQueryService = loginLogQueryService;
    }

    @GetMapping
    @SaCheckPermission("sys:audit:read")
    public R<Page<SysLoginLogEntity>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return R.ok(loginLogQueryService.page(page, size));
    }
}
