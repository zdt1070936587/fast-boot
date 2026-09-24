package com.fastboot.modules.user.interfaces.auth;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fastboot.common.api.R;
import com.fastboot.modules.user.application.AuthAppService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户组织归属相关 HTTP 接口。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserOrgController {

    /** 认证应用服务 */
    private final AuthAppService authAppService;

    public UserOrgController(AuthAppService authAppService) {
        this.authAppService = authAppService;
    }

    @PutMapping("/{userId}/org/{orgUnitId}")
    @SaCheckPermission("sys:org:write")
    public R<Void> assignOrg(@PathVariable Long userId, @PathVariable Long orgUnitId) {
        authAppService.assignOrg(userId, orgUnitId);
        return R.ok();
    }
}
