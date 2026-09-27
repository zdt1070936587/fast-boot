package com.fastboot.modules.system.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysNoticeEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * SysNoticeEntity Mapper。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Mapper
public interface SysNoticeMapper extends BaseMapper<SysNoticeEntity> {
}
