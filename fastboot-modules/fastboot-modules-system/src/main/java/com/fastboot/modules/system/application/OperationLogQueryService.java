package com.fastboot.modules.system.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fastboot.modules.system.infrastructure.mapper.SysOperationLogMapper;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysOperationLogEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 操作日志查询。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Service
public class OperationLogQueryService {

    private final SysOperationLogMapper operationLogMapper;

    public OperationLogQueryService(SysOperationLogMapper operationLogMapper) {
        this.operationLogMapper = operationLogMapper;
    }

    @Transactional(readOnly = true)
    public Page<SysOperationLogEntity> page(int pageNo, int pageSize) {
        return operationLogMapper.selectPage(
                new Page<>(pageNo, pageSize),
                Wrappers.<SysOperationLogEntity>lambdaQuery()
                        .orderByDesc(SysOperationLogEntity::getCreatedAt));
    }
}
