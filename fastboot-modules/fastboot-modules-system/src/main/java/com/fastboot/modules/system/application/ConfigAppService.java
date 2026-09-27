package com.fastboot.modules.system.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.modules.system.api.ConfigPort;
import com.fastboot.modules.system.infrastructure.mapper.SysConfigMapper;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysConfigEntity;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统配置应用服务。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Service
public class ConfigAppService implements ConfigPort {

    private final SysConfigMapper configMapper;

    public ConfigAppService(SysConfigMapper configMapper) {
        this.configMapper = configMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<String> get(String key) {
        SysConfigEntity entity =
                configMapper.selectOne(
                        Wrappers.<SysConfigEntity>lambdaQuery().eq(SysConfigEntity::getConfigKey, key));
        return entity == null ? Optional.empty() : Optional.ofNullable(entity.getConfigValue());
    }

    @Override
    @Transactional
    public void set(String key, String value) {
        SysConfigEntity entity =
                configMapper.selectOne(
                        Wrappers.<SysConfigEntity>lambdaQuery().eq(SysConfigEntity::getConfigKey, key));
        if (entity == null) {
            entity = new SysConfigEntity();
            entity.setConfigKey(key);
            entity.setConfigValue(value);
            entity.setRefreshable(true);
            configMapper.insert(entity);
            return;
        }
        entity.setConfigValue(value);
        if (configMapper.updateById(entity) == 0) {
            throw new BizException(ErrorCode.CONFLICT, "数据已被修改");
        }
    }

    @Transactional(readOnly = true)
    public SysConfigEntity findEntity(String key) {
        return configMapper.selectOne(
                Wrappers.<SysConfigEntity>lambdaQuery().eq(SysConfigEntity::getConfigKey, key));
    }

    @Transactional
    public Long create(SysConfigEntity entity) {
        configMapper.insert(entity);
        return entity.getId();
    }

    @Transactional
    public void update(SysConfigEntity entity) {
        if (configMapper.updateById(entity) == 0) {
            throw new BizException(ErrorCode.CONFLICT, "数据已被修改");
        }
    }

    @Transactional(readOnly = true)
    public java.util.List<SysConfigEntity> listAll() {
        return configMapper.selectList(Wrappers.emptyWrapper());
    }
}
