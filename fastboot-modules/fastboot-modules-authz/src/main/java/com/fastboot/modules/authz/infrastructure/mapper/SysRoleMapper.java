package com.fastboot.modules.authz.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fastboot.modules.authz.infrastructure.persistence.entity.SysRoleEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统角色 MyBatis 映射器
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRoleEntity> {}
