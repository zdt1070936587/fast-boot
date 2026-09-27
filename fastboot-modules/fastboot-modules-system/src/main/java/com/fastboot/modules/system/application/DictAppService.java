package com.fastboot.modules.system.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fastboot.modules.system.api.DictQueryPort;
import com.fastboot.modules.system.infrastructure.mapper.SysDictDataMapper;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysDictDataEntity;
import com.fastboot.starter.tenant.TenantKeyPrefix;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 字典应用服务。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Service
public class DictAppService implements DictQueryPort {

    private static final Duration TTL = Duration.ofHours(1);

    private final SysDictDataMapper dictDataMapper;
    private final RedissonClient redissonClient;

    public DictAppService(SysDictDataMapper dictDataMapper, RedissonClient redissonClient) {
        this.dictDataMapper = dictDataMapper;
        this.redissonClient = redissonClient;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DictItem> data(String typeCode) {
        String key = TenantKeyPrefix.of("fb:dict:" + typeCode);
        RBucket<List<DictItem>> bucket = redissonClient.getBucket(key);
        List<DictItem> cached = bucket.get();
        if (cached != null) {
            return cached;
        }
        List<DictItem> items =
                dictDataMapper
                        .selectList(
                                Wrappers.<SysDictDataEntity>lambdaQuery()
                                        .eq(SysDictDataEntity::getTypeCode, typeCode)
                                        .eq(SysDictDataEntity::getEnabled, true))
                        .stream()
                        .sorted(Comparator.comparingInt(SysDictDataEntity::getSortNo))
                        .map(e -> new DictItem(e.getLabel(), e.getValue(), e.getSortNo()))
                        .toList();
        bucket.set(items, TTL);
        return items;
    }

    @Transactional
    public void evictCache(String typeCode) {
        redissonClient.getBucket(TenantKeyPrefix.of("fb:dict:" + typeCode)).delete();
    }
}
