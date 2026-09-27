package com.fastboot.modules.infra.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fastboot.modules.infra.infrastructure.persistence.entity.SysApiAccessLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * SysApiAccessLogEntity Mapper。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Mapper
public interface SysApiAccessLogMapper extends BaseMapper<SysApiAccessLogEntity> {
}
