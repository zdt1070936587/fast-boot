package com.fastboot.service.org.authz;

import com.fastboot.modules.authz.api.DataScopePort;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * 组织微服务在未接入授权 Feign 时的数据范围占位：空列表表示不限制组织可见性。
 *
 * <p>TODO: 改为使用 {@code com.fastboot.cloud.remote.authz.RemoteDataScopeAdapter} 并启用 Feign。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Component
@ConditionalOnMissingBean(DataScopePort.class)
public class RemoteDataScopeAdapterStub implements DataScopePort {

    @Override
    public List<Long> visibleOrgIds(Long userId, Long selfOrgId) {
        return Collections.emptyList();
    }
}
