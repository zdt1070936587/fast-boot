package com.fastboot.modules.user.interfaces.demo;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fastboot.common.api.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 演示用受权限保护的示例接口。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@RestController
@RequestMapping("/api/v1/demo")
public class DemoProtectedController {

    @GetMapping("/ping")
    @SaCheckPermission("sys:demo:read")
    public R<String> ping() {
        return R.ok("pong");
    }
}
