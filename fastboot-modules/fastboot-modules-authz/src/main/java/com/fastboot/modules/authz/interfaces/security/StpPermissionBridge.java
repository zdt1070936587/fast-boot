package com.fastboot.modules.authz.interfaces.security;

import cn.dev33.satoken.stp.StpInterface;
import com.fastboot.modules.authz.api.PermissionQueryPort;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class StpPermissionBridge implements StpInterface {

  private final PermissionQueryPort permissionQueryPort;

  public StpPermissionBridge(PermissionQueryPort permissionQueryPort) {
    this.permissionQueryPort = permissionQueryPort;
  }

  @Override
  public List<String> getPermissionList(Object loginId, String loginType) {
    if (loginId == null) {
      return Collections.emptyList();
    }
    return permissionQueryPort.listByUserId(Long.valueOf(String.valueOf(loginId)));
  }

  @Override
  public List<String> getRoleList(Object loginId, String loginType) {
    if (loginId == null) {
      return Collections.emptyList();
    }
    return permissionQueryPort.listRolesByUserId(Long.valueOf(String.valueOf(loginId)));
  }
}
