package com.fastboot.modules.system.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.modules.system.infrastructure.mapper.SysErrorCodeMapper;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysErrorCodeEntity;
import com.fastboot.starter.tenant.TenantKeyPrefix;
import java.time.Duration;
import java.util.List;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 业务错误码文案应用服务。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Service
public class ErrorCodeAppService {

    private static final Duration TTL = Duration.ofHours(1);

    private final SysErrorCodeMapper errorCodeMapper;
    private final RedissonClient redissonClient;

    public ErrorCodeAppService(SysErrorCodeMapper errorCodeMapper, RedissonClient redissonClient) {
        this.errorCodeMapper = errorCodeMapper;
        this.redissonClient = redissonClient;
    }

    @Transactional(readOnly = true)
    public List<SysErrorCodeEntity> list() {
        return errorCodeMapper.selectList(null);
    }

    @Transactional(readOnly = true)
    public SysErrorCodeEntity get(Long id) {
        SysErrorCodeEntity entity = errorCodeMapper.selectById(id);
        if (entity == null) {
            throw new BizException(ErrorCode.NOT_FOUND);
        }
        return entity;
    }

    @Transactional
    public Long create(SysErrorCodeEntity entity) {
        errorCodeMapper.insert(entity);
        evict(entity.getCode(), entity.getLocale());
        return entity.getId();
    }

    @Transactional
    public void update(SysErrorCodeEntity entity) {
        if (errorCodeMapper.updateById(entity) == 0) {
            throw new BizException(ErrorCode.CONFLICT, "数据已被修改");
        }
        evict(entity.getCode(), entity.getLocale());
    }

    @Transactional
    public void delete(Long id) {
        SysErrorCodeEntity entity = errorCodeMapper.selectById(id);
        if (entity != null) {
            errorCodeMapper.deleteById(id);
            evict(entity.getCode(), entity.getLocale());
        }
    }

    @Transactional(readOnly = true)
    public SysErrorCodeEntity findByCodeAndLocale(int code, String locale) {
        return errorCodeMapper.selectOne(
                Wrappers.<SysErrorCodeEntity>lambdaQuery()
                        .eq(SysErrorCodeEntity::getCode, code)
                        .eq(SysErrorCodeEntity::getLocale, locale));
    }

    private void evict(int code, String locale) {
        redissonClient.getBucket(TenantKeyPrefix.of("fb:errmsg:" + code + ":" + locale)).delete();
    }
}
