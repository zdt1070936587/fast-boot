package com.fastboot.modules.system.application;

import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.modules.system.infrastructure.mapper.SysDictDataMapper;
import com.fastboot.modules.system.infrastructure.mapper.SysDictTypeMapper;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysDictDataEntity;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysDictTypeEntity;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 字典类型与字典项维护。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Service
public class DictTypeAppService {

    private final SysDictTypeMapper dictTypeMapper;
    private final SysDictDataMapper dictDataMapper;
    private final DictAppService dictAppService;

    public DictTypeAppService(
            SysDictTypeMapper dictTypeMapper,
            SysDictDataMapper dictDataMapper,
            DictAppService dictAppService) {
        this.dictTypeMapper = dictTypeMapper;
        this.dictDataMapper = dictDataMapper;
        this.dictAppService = dictAppService;
    }

    @Transactional(readOnly = true)
    public List<SysDictTypeEntity> listTypes() {
        return dictTypeMapper.selectList(null);
    }

    @Transactional
    public Long createType(SysDictTypeEntity entity) {
        dictTypeMapper.insert(entity);
        return entity.getId();
    }

    @Transactional
    public void updateType(SysDictTypeEntity entity) {
        if (dictTypeMapper.updateById(entity) == 0) {
            throw new BizException(ErrorCode.CONFLICT, "数据已被修改");
        }
        dictAppService.evictCache(entity.getCode());
    }

    @Transactional(readOnly = true)
    public List<SysDictDataEntity> listData(String typeCode) {
        return dictDataMapper.selectList(
                com.baomidou.mybatisplus.core.toolkit.Wrappers.<SysDictDataEntity>lambdaQuery()
                        .eq(SysDictDataEntity::getTypeCode, typeCode));
    }

    @Transactional
    public Long createData(SysDictDataEntity entity) {
        dictDataMapper.insert(entity);
        dictAppService.evictCache(entity.getTypeCode());
        return entity.getId();
    }

    @Transactional
    public void updateData(SysDictDataEntity entity) {
        if (dictDataMapper.updateById(entity) == 0) {
            throw new BizException(ErrorCode.CONFLICT, "数据已被修改");
        }
        dictAppService.evictCache(entity.getTypeCode());
    }
}
