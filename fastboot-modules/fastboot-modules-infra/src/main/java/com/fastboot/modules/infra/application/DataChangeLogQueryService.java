package com.fastboot.modules.infra.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fastboot.modules.infra.infrastructure.mapper.SysDataChangeLogMapper;
import com.fastboot.modules.infra.infrastructure.persistence.entity.SysDataChangeLogEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据变更日志查询。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Service
public class DataChangeLogQueryService {

    private final SysDataChangeLogMapper dataChangeLogMapper;

    public DataChangeLogQueryService(SysDataChangeLogMapper dataChangeLogMapper) {
        this.dataChangeLogMapper = dataChangeLogMapper;
    }

    @Transactional(readOnly = true)
    public Page<SysDataChangeLogEntity> page(int pageNo, int pageSize) {
        return dataChangeLogMapper.selectPage(
                new Page<>(pageNo, pageSize),
                Wrappers.<SysDataChangeLogEntity>lambdaQuery()
                        .orderByDesc(SysDataChangeLogEntity::getCreatedAt));
    }
}
