package com.fastboot.cloud.remote.authz;

import com.fastboot.common.api.R;
import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.modules.authz.api.DataScopePort;
import com.fastboot.modules.authz.api.DataScopeSnapshot;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 通过 Feign 实现的 {@link DataScopePort}。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Component
@ConditionalOnClass(name = "org.springframework.cloud.openfeign.FeignClient")
@ConditionalOnProperty(prefix = "fastboot.remote.authz", name = "enabled", havingValue = "true")
public class RemoteDataScopeAdapter implements DataScopePort {

    private final AuthzPermissionFeignClient client;

    public RemoteDataScopeAdapter(AuthzPermissionFeignClient client) {
        this.client = client;
    }

    @Override
    public List<Long> visibleOrgIds(Long userId, Long selfOrgId) {
        R<DataScopeSnapshot> response = client.dataScope(userId, selfOrgId);
        if (response == null || response.getData() == null) {
            throw new BizException(ErrorCode.INTERNAL_ERROR, "授权服务返回空数据范围");
        }
        List<Long> ids = response.getData().visibleOrgIds();
        return ids == null ? Collections.emptyList() : ids;
    }
}
