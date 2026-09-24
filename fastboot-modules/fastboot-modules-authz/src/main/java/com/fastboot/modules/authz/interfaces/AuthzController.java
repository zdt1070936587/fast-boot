package com.fastboot.modules.authz.interfaces;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fastboot.common.api.PageData;
import com.fastboot.common.api.R;
import com.fastboot.modules.authz.application.AuthzAppService;
import com.fastboot.modules.authz.application.AuthzCatalogService;
import com.fastboot.modules.authz.interfaces.dto.IdListRequest;
import com.fastboot.modules.authz.interfaces.dto.MenuCreateRequest;
import com.fastboot.modules.authz.interfaces.dto.MenuView;
import com.fastboot.modules.authz.interfaces.dto.PermissionCreateRequest;
import com.fastboot.modules.authz.interfaces.dto.PermissionView;
import com.fastboot.modules.authz.interfaces.dto.RoleCreateRequest;
import com.fastboot.modules.authz.interfaces.dto.RoleView;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RBAC 授权管理 REST 接口
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@RestController
@RequestMapping("/api/v1/authz")
@Tag(name = "authz")
public class AuthzController {

    /** 角色/权限/菜单目录服务 */
    private final AuthzCatalogService catalogService;
    /** 用户角色与授权关系应用服务 */
    private final AuthzAppService authzAppService;

    public AuthzController(AuthzCatalogService catalogService, AuthzAppService authzAppService) {
        this.catalogService = catalogService;
        this.authzAppService = authzAppService;
    }

    @GetMapping("/roles")
    @SaCheckPermission("sys:authz:read")
    public R<PageData<RoleView>> roles() {
        return R.ok(catalogService.listRoles());
    }

    @PostMapping("/roles")
    @SaCheckPermission("sys:authz:write")
    public R<Long> createRole(@Valid @RequestBody RoleCreateRequest request) {
        return R.ok(catalogService.createRole(request.code(), request.name()));
    }

    @GetMapping("/permissions")
    @SaCheckPermission("sys:authz:read")
    public R<PageData<PermissionView>> permissions() {
        return R.ok(catalogService.listPermissions());
    }

    @PostMapping("/permissions")
    @SaCheckPermission("sys:authz:write")
    public R<Long> createPermission(@Valid @RequestBody PermissionCreateRequest request) {
        return R.ok(catalogService.createPermission(request.permType(), request.permCode(), request.name()));
    }

    @GetMapping("/menus")
    @SaCheckPermission("sys:authz:read")
    public R<List<MenuView>> menus() {
        return R.ok(catalogService.listMenus());
    }

    @PostMapping("/menus")
    @SaCheckPermission("sys:authz:write")
    public R<Long> createMenu(@Valid @RequestBody MenuCreateRequest request) {
        return R.ok(
                catalogService.createMenu(
                        new MenuView(
                                null,
                                request.parentId(),
                                request.name(),
                                request.path(),
                                request.component(),
                                request.permCode())));
    }

    @PutMapping("/users/{userId}/roles")
    @SaCheckPermission("sys:authz:write")
    public R<Void> assignRoles(@PathVariable Long userId, @Valid @RequestBody IdListRequest request) {
        authzAppService.assignRoles(userId, request.ids());
        return R.ok();
    }

    @PutMapping("/roles/{roleId}/permissions")
    @SaCheckPermission("sys:authz:write")
    public R<Void> assignPermissions(@PathVariable Long roleId, @Valid @RequestBody IdListRequest request) {
        authzAppService.assignPermissions(roleId, request.ids());
        return R.ok();
    }

    @PutMapping("/roles/{roleId}/menus")
    @SaCheckPermission("sys:authz:write")
    public R<Void> assignMenus(@PathVariable Long roleId, @Valid @RequestBody IdListRequest request) {
        authzAppService.assignMenus(roleId, request.ids());
        return R.ok();
    }
}
