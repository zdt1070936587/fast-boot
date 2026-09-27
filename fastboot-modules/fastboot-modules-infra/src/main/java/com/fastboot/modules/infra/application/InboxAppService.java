package com.fastboot.modules.infra.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fastboot.modules.infra.infrastructure.mapper.SysInboxMapper;
import com.fastboot.modules.infra.infrastructure.persistence.entity.SysInboxEntity;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 站内信查询。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Service
public class InboxAppService {

    private final SysInboxMapper inboxMapper;

    public InboxAppService(SysInboxMapper inboxMapper) {
        this.inboxMapper = inboxMapper;
    }

    @Transactional(readOnly = true)
    public List<SysInboxEntity> listForUser(Long userId) {
        return inboxMapper.selectList(
                Wrappers.<SysInboxEntity>lambdaQuery()
                        .eq(SysInboxEntity::getUserId, userId)
                        .orderByDesc(SysInboxEntity::getCreatedAt));
    }

    @Transactional
    public void markRead(Long id) {
        SysInboxEntity entity = inboxMapper.selectById(id);
        if (entity != null) {
            entity.setReadFlag(true);
            inboxMapper.updateById(entity);
        }
    }
}
