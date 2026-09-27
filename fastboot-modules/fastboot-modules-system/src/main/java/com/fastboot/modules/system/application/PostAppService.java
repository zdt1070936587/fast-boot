package com.fastboot.modules.system.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.modules.system.infrastructure.mapper.SysPostMapper;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysPostEntity;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 岗位应用服务。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Service
public class PostAppService {

    private final SysPostMapper postMapper;

    public PostAppService(SysPostMapper postMapper) {
        this.postMapper = postMapper;
    }

    @Transactional(readOnly = true)
    public List<SysPostEntity> list() {
        return postMapper.selectList(
                Wrappers.<SysPostEntity>lambdaQuery().orderByAsc(SysPostEntity::getSortNo));
    }

    @Transactional(readOnly = true)
    public SysPostEntity get(Long id) {
        SysPostEntity entity = postMapper.selectById(id);
        if (entity == null) {
            throw new BizException(ErrorCode.NOT_FOUND);
        }
        return entity;
    }

    @Transactional
    public Long create(SysPostEntity entity) {
        postMapper.insert(entity);
        return entity.getId();
    }

    @Transactional
    public void update(SysPostEntity entity) {
        if (postMapper.updateById(entity) == 0) {
            throw new BizException(ErrorCode.CONFLICT, "数据已被修改");
        }
    }

    @Transactional
    public void delete(Long id) {
        postMapper.deleteById(id);
    }
}
