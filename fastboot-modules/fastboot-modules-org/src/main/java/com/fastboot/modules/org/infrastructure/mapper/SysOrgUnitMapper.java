package com.fastboot.modules.org.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fastboot.modules.org.infrastructure.persistence.entity.SysOrgUnitEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 组织单元 MyBatis 映射器
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Mapper
public interface SysOrgUnitMapper extends BaseMapper<SysOrgUnitEntity> {}
