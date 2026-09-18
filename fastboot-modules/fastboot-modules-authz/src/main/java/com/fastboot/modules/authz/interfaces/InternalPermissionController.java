package com.fastboot.modules.authz.interfaces;

import com.fastboot.common.api.R;
import com.fastboot.modules.authz.api.PermissionQueryPort;
import com.fastboot.modules.authz.api.PermissionSnapshot;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 内部服务用的用户权限快照接口
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@RestController
@RequestMapping("/api/v1/internal/permissions")
public class InternalPermissionController {

  /** 权限查询端口 */
  private final PermissionQueryPort permissionQueryPort;

  public InternalPermissionController(PermissionQueryPort permissionQueryPort) {
    this.permissionQueryPort = permissionQueryPort;
  }

  @GetMapping("/{userId}")
  public R<PermissionSnapshot> get(@PathVariable Long userId) {
    return R.ok(
        new PermissionSnapshot(
            userId,
            permissionQueryPort.listByUserId(userId),
            permissionQueryPort.listRolesByUserId(userId)));
  }
}
