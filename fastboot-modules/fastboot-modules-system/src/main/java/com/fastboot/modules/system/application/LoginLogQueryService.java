package com.fastboot.modules.system.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fastboot.modules.system.infrastructure.mapper.SysLoginLogMapper;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysLoginLogEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 登录日志查询。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Service
public class LoginLogQueryService {

    private final SysLoginLogMapper loginLogMapper;

    public LoginLogQueryService(SysLoginLogMapper loginLogMapper) {
        this.loginLogMapper = loginLogMapper;
    }

    @Transactional(readOnly = true)
    public Page<SysLoginLogEntity> page(int pageNo, int pageSize) {
        return loginLogMapper.selectPage(
                new Page<>(pageNo, pageSize),
                Wrappers.<SysLoginLogEntity>lambdaQuery().orderByDesc(SysLoginLogEntity::getCreatedAt));
    }
}
