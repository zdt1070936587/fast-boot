package com.fastboot.modules.user.interfaces.auth;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fastboot.common.api.R;
import com.fastboot.modules.user.application.AuthAppService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserOrgController {

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
