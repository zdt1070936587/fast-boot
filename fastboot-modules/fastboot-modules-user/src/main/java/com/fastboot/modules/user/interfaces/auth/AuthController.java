package com.fastboot.modules.user.interfaces.auth;

import cn.dev33.satoken.stp.StpUtil;
import com.fastboot.common.api.R;
import com.fastboot.modules.authz.api.MenuSnapshot;
import com.fastboot.modules.user.application.AuthAppService;
import com.fastboot.modules.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证相关 HTTP 接口（登录、登出、当前用户信息）。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "auth")
public class AuthController {

    /** 认证应用服务 */
    private final AuthAppService authAppService;

    public AuthController(AuthAppService authAppService) {
        this.authAppService = authAppService;
    }

    @PostMapping("/login")
    @Operation(summary = "登录")
    public R<LoginResponse> login(@Valid @RequestBody LoginRequest request, HttpServletRequest http) {
        String token = authAppService.login(request.username(), request.password(), clientIp(http), http);
        return R.ok(new LoginResponse(StpUtil.getTokenName(), token));
    }

    @PostMapping("/logout")
    @Operation(summary = "登出")
    public R<Void> logout() {
        authAppService.logout();
        return R.ok();
    }

    @GetMapping("/menus")
    @Operation(summary = "当前用户菜单")
    public R<List<MenuSnapshot>> menus() {
        return R.ok(authAppService.currentMenus());
    }

    @GetMapping("/info")
    @Operation(summary = "当前登录用户")
    public R<AuthInfoResponse> info() {
        User user = authAppService.currentUser();
        return R.ok(
                new AuthInfoResponse(user.getId(), user.getUsername(), user.getDisplayName(), user.getOrgUnitId()));
    }

    /** 仅使用容器解析后的远端地址；受信代理需配置 server.forward-headers-strategy */
    private static String clientIp(HttpServletRequest request) {
        return request.getRemoteAddr() == null ? "unknown" : request.getRemoteAddr();
    }
}
