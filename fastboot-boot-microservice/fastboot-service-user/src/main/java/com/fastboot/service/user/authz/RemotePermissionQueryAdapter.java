package com.fastboot.service.user.authz;

import com.fastboot.common.api.R;
import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.modules.authz.api.PermissionQueryPort;
import com.fastboot.modules.authz.api.PermissionSnapshot;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 通过 Feign 实现的 {@link PermissionQueryPort}。
 *
 * @author zhaodt
 * @since 2026-09-24
 */
@Component
public class RemotePermissionQueryAdapter implements PermissionQueryPort {

    private final AuthzPermissionClient client;

    public RemotePermissionQueryAdapter(AuthzPermissionClient client) {
        this.client = client;
    }

    @Override
    public List<String> listByUserId(Long userId) {
        return snapshot(userId).permissions();
    }

    @Override
    public List<String> listRolesByUserId(Long userId) {
        return snapshot(userId).roles();
    }

    @Override
    public void evict(Long userId) {
        client.evictCache(userId);
    }

    private PermissionSnapshot snapshot(Long userId) {
        R<PermissionSnapshot> response = client.get(userId);
        if (response == null || response.getData() == null) {
            throw new BizException(ErrorCode.INTERNAL_ERROR, "授权服务返回空权限快照");
        }
        return response.getData();
    }
}
