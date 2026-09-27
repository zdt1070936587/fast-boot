package com.fastboot.modules.infra.application.notify;

import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.modules.infra.infrastructure.mapper.SysInboxMapper;
import com.fastboot.modules.infra.infrastructure.persistence.entity.SysInboxEntity;
import org.springframework.stereotype.Component;

/**
 * 站内信通知渠道。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Component
public class InboxChannel implements NotifyChannel {

    public static final String CHANNEL = "INBOX";

    private final SysInboxMapper inboxMapper;

    public InboxChannel(SysInboxMapper inboxMapper) {
        this.inboxMapper = inboxMapper;
    }

    @Override
    public String channel() {
        return CHANNEL;
    }

    @Override
    public void send(String receiver, String title, String content) {
        long userId;
        try {
            userId = Long.parseLong(receiver);
        } catch (NumberFormatException ex) {
            throw new BizException(ErrorCode.VALIDATION_FAILED, "站内信 receiver 须为用户 ID");
        }
        SysInboxEntity entity = new SysInboxEntity();
        entity.setUserId(userId);
        entity.setTitle(title);
        entity.setContent(content);
        entity.setReadFlag(false);
        inboxMapper.insert(entity);
    }
}
