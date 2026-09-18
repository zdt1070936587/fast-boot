package com.fastboot.modules.authz.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fastboot.modules.authz.infrastructure.persistence.entity.SysPermissionEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统权限点 MyBatis 映射器
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermissionEntity> {}
