package com.fastboot.modules.authz.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fastboot.modules.authz.infrastructure.persistence.entity.SysMenuEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统菜单 MyBatis 映射器
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenuEntity> {}
