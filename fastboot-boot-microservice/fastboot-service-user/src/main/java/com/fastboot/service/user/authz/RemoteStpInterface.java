package com.fastboot.service.user.authz;

import cn.dev33.satoken.stp.StpInterface;
import com.fastboot.modules.authz.api.PermissionQueryPort;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 微服务场景下的 Sa-Token 权限桥接（基于远程 PermissionQueryPort）。
 *
 * @author zhaodt
 * @since 2026-09-24
 */
@Component
public class RemoteStpInterface implements StpInterface {

    private final PermissionQueryPort permissionQueryPort;

    public RemoteStpInterface(PermissionQueryPort permissionQueryPort) {
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
