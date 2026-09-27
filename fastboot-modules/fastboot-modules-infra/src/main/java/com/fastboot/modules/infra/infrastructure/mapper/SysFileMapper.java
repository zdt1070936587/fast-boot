package com.fastboot.modules.infra.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fastboot.modules.infra.infrastructure.persistence.entity.SysFileEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * SysFileEntity Mapper。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Mapper
public interface SysFileMapper extends BaseMapper<SysFileEntity> {
}
