package com.fastboot.modules.authz.interfaces;

import com.fastboot.common.api.R;
import com.fastboot.modules.authz.api.DataScopePort;
import com.fastboot.modules.authz.api.DataScopeSnapshot;
import com.fastboot.modules.authz.api.PermissionQueryPort;
import com.fastboot.modules.authz.api.PermissionSnapshot;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 内部服务用的用户权限与数据范围接口
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@RestController
@RequestMapping("/api/v1/internal")
public class InternalPermissionController {

    /** 权限查询端口 */
    private final PermissionQueryPort permissionQueryPort;
    /** 数据范围端口 */
    private final DataScopePort dataScopePort;

    public InternalPermissionController(
            PermissionQueryPort permissionQueryPort, DataScopePort dataScopePort) {
        this.permissionQueryPort = permissionQueryPort;
        this.dataScopePort = dataScopePort;
    }

    @GetMapping("/permissions/{userId}")
    public R<PermissionSnapshot> get(@PathVariable Long userId) {
        return R.ok(
                new PermissionSnapshot(
                        userId,
                        permissionQueryPort.listByUserId(userId),
                        permissionQueryPort.listRolesByUserId(userId)));
    }

    @GetMapping("/data-scope/{userId}")
    public R<DataScopeSnapshot> dataScope(
            @PathVariable Long userId, @RequestParam(required = false) Long selfOrgId) {
        return R.ok(new DataScopeSnapshot(userId, dataScopePort.visibleOrgIds(userId, selfOrgId)));
    }

    @DeleteMapping("/permissions/{userId}/cache")
    public R<Void> evictCache(@PathVariable Long userId) {
        permissionQueryPort.evict(userId);
        return R.ok();
    }
}
