package com.fastboot.modules.authz.api;

import java.util.List;

public interface PermissionQueryPort {
  List<String> listByUserId(Long userId);

  List<String> listRolesByUserId(Long userId);

  void evict(Long userId);
}
