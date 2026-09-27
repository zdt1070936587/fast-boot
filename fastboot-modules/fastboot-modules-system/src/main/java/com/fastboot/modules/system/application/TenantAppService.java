package com.fastboot.modules.system.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.modules.system.api.TenantQueryPort;
import com.fastboot.modules.system.infrastructure.mapper.SysTenantMapper;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysTenantEntity;
import com.fastboot.starter.tenant.TenantIgnore;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 租户应用服务。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Service
public class TenantAppService implements TenantQueryPort {

    private final SysTenantMapper tenantMapper;

    public TenantAppService(SysTenantMapper tenantMapper) {
        this.tenantMapper = tenantMapper;
    }

    @Override
    @TenantIgnore
    @Transactional(readOnly = true)
    public Optional<TenantSnapshot> find(Long id) {
        SysTenantEntity entity = tenantMapper.selectById(id);
        return Optional.ofNullable(entity).map(this::toSnapshot);
    }

    @Override
    @TenantIgnore
    @Transactional(readOnly = true)
    public Optional<TenantSnapshot> findByCode(String code) {
        SysTenantEntity entity =
                tenantMapper.selectOne(
                        Wrappers.<SysTenantEntity>lambdaQuery().eq(SysTenantEntity::getCode, code));
        return Optional.ofNullable(entity).map(this::toSnapshot);
    }

    @TenantIgnore
    @Transactional(readOnly = true)
    public List<SysTenantEntity> list() {
        return tenantMapper.selectList(Wrappers.emptyWrapper());
    }

    @TenantIgnore
    @Transactional
    public Long create(SysTenantEntity entity) {
        tenantMapper.insert(entity);
        return entity.getId();
    }

    @TenantIgnore
    @Transactional
    public void update(SysTenantEntity entity) {
        if (tenantMapper.updateById(entity) == 0) {
            throw new BizException(ErrorCode.CONFLICT, "数据已被修改");
        }
    }

    private TenantSnapshot toSnapshot(SysTenantEntity entity) {
        return new TenantSnapshot(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getPackageId(),
                entity.getStatus(),
                entity.getExpireAt());
    }
}
