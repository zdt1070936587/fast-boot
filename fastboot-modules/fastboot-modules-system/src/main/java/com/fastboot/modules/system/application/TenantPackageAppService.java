package com.fastboot.modules.system.application;

import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.modules.system.infrastructure.mapper.SysTenantPackageMapper;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysTenantPackageEntity;
import com.fastboot.starter.tenant.TenantIgnore;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 租户套餐应用服务。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Service
public class TenantPackageAppService {

    private final SysTenantPackageMapper packageMapper;

    public TenantPackageAppService(SysTenantPackageMapper packageMapper) {
        this.packageMapper = packageMapper;
    }

    @TenantIgnore
    @Transactional(readOnly = true)
    public List<SysTenantPackageEntity> list() {
        return packageMapper.selectList(null);
    }

    @TenantIgnore
    @Transactional(readOnly = true)
    public SysTenantPackageEntity get(Long id) {
        SysTenantPackageEntity entity = packageMapper.selectById(id);
        if (entity == null) {
            throw new BizException(ErrorCode.NOT_FOUND);
        }
        return entity;
    }

    @TenantIgnore
    @Transactional
    public Long create(SysTenantPackageEntity entity) {
        packageMapper.insert(entity);
        return entity.getId();
    }

    @TenantIgnore
    @Transactional
    public void update(SysTenantPackageEntity entity) {
        if (packageMapper.updateById(entity) == 0) {
            throw new BizException(ErrorCode.CONFLICT, "数据已被修改");
        }
    }
}
