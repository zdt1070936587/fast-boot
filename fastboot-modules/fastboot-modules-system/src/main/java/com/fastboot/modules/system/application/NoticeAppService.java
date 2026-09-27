package com.fastboot.modules.system.application;

import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.modules.system.infrastructure.mapper.SysNoticeMapper;
import com.fastboot.modules.system.infrastructure.persistence.entity.SysNoticeEntity;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 通知公告应用服务。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Service
public class NoticeAppService {

    private final SysNoticeMapper noticeMapper;

    public NoticeAppService(SysNoticeMapper noticeMapper) {
        this.noticeMapper = noticeMapper;
    }

    @Transactional(readOnly = true)
    public List<SysNoticeEntity> list() {
        return noticeMapper.selectList(null);
    }

    @Transactional(readOnly = true)
    public SysNoticeEntity get(Long id) {
        SysNoticeEntity entity = noticeMapper.selectById(id);
        if (entity == null) {
            throw new BizException(ErrorCode.NOT_FOUND);
        }
        return entity;
    }

    @Transactional
    public Long create(SysNoticeEntity entity) {
        noticeMapper.insert(entity);
        return entity.getId();
    }

    @Transactional
    public void update(SysNoticeEntity entity) {
        if (noticeMapper.updateById(entity) == 0) {
            throw new BizException(ErrorCode.CONFLICT, "数据已被修改");
        }
    }

    @Transactional
    public void delete(Long id) {
        noticeMapper.deleteById(id);
    }
}
