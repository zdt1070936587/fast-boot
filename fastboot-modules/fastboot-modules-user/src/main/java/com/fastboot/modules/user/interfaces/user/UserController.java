package com.fastboot.modules.user.interfaces.user;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fastboot.common.api.R;
import com.fastboot.modules.user.application.UserAppService;
import com.fastboot.modules.user.application.dto.UserView;
import com.fastboot.modules.user.interfaces.user.dto.UserCreateRequest;
import com.fastboot.modules.user.interfaces.user.dto.UserPasswordRequest;
import com.fastboot.modules.user.interfaces.user.dto.UserUpdateRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理 REST 接口。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "user")
public class UserController {

    private final UserAppService userAppService;

    public UserController(UserAppService userAppService) {
        this.userAppService = userAppService;
    }

    @GetMapping
    @SaCheckPermission("sys:user:read")
    public R<List<UserView>> list() {
        return R.ok(userAppService.list());
    }

    @GetMapping("/{id}")
    @SaCheckPermission("sys:user:read")
    public R<UserView> get(@PathVariable Long id) {
        return R.ok(userAppService.get(id));
    }

    @PostMapping
    @SaCheckPermission("sys:user:write")
    public R<Long> create(@Valid @RequestBody UserCreateRequest request) {
        return R.ok(
                userAppService.create(
                        request.username(), request.displayName(), request.password(), request.orgUnitId()));
    }

    @PutMapping("/{id}")
    @SaCheckPermission("sys:user:write")
    public R<Void> update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request) {
        userAppService.update(id, request.displayName(), request.enabled(), request.orgUnitId());
        return R.ok();
    }

    @PutMapping("/{id}/password")
    @SaCheckPermission("sys:user:write")
    public R<Void> resetPassword(@PathVariable Long id, @Valid @RequestBody UserPasswordRequest request) {
        userAppService.resetPassword(id, request.password());
        return R.ok();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("sys:user:write")
    public R<Void> delete(@PathVariable Long id) {
        userAppService.delete(id);
        return R.ok();
    }
}
