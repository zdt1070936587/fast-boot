package com.fastboot.modules.system.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysTenantPackageEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * SysTenantPackageEntity Mapper。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Mapper
public interface SysTenantPackageMapper extends BaseMapper<SysTenantPackageEntity> {
}
