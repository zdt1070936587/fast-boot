package com.fastboot.modules.infra.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fastboot.modules.infra.infrastructure.persistence.entity.SysJobLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * SysJobLogEntity Mapper。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Mapper
public interface SysJobLogMapper extends BaseMapper<SysJobLogEntity> {
}
