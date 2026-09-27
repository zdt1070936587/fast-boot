package com.fastboot.modules.infra.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.modules.infra.api.NotifyPort;
import com.fastboot.modules.infra.application.notify.NotifyChannel;
import com.fastboot.modules.infra.infrastructure.mapper.SysNotifyMessageMapper;
import com.fastboot.modules.infra.infrastructure.mapper.SysNotifyTemplateMapper;
import com.fastboot.modules.infra.infrastructure.persistence.entity.SysNotifyMessageEntity;
import com.fastboot.modules.infra.infrastructure.persistence.entity.SysNotifyTemplateEntity;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 通知发送应用服务。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Service
public class NotifyAppService implements NotifyPort {

    private final SysNotifyTemplateMapper templateMapper;
    private final SysNotifyMessageMapper messageMapper;
    private final List<NotifyChannel> channels;

    public NotifyAppService(
            SysNotifyTemplateMapper templateMapper,
            SysNotifyMessageMapper messageMapper,
            List<NotifyChannel> channels) {
        this.templateMapper = templateMapper;
        this.messageMapper = messageMapper;
        this.channels = channels;
    }

    @Override
    @Transactional
    public void send(String channel, String templateCode, String receiver, Map<String, Object> params) {
        SysNotifyTemplateEntity template =
                templateMapper.selectOne(
                        Wrappers.<SysNotifyTemplateEntity>lambdaQuery()
                                .eq(SysNotifyTemplateEntity::getCode, templateCode)
                                .eq(SysNotifyTemplateEntity::getChannel, channel));
        if (template == null) {
            throw new BizException(ErrorCode.NOT_FOUND, "通知模板不存在");
        }
        String title = render(template.getTitle(), params);
        String content = render(template.getContent(), params);
        NotifyChannel target =
                channels.stream()
                        .filter(c -> c.channel().equalsIgnoreCase(channel))
                        .findFirst()
                        .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND, "通知渠道未注册"));
        target.send(receiver, title, content);
        SysNotifyMessageEntity log = new SysNotifyMessageEntity();
        log.setTemplateCode(templateCode);
        log.setChannel(channel);
        log.setReceiver(receiver);
        log.setTitle(title);
        log.setContent(content);
        log.setStatus("SENT");
        messageMapper.insert(log);
    }

    private String render(String template, Map<String, Object> params) {
        if (template == null) {
            return null;
        }
        String result = template;
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                result = result.replace("${" + entry.getKey() + "}", String.valueOf(entry.getValue()));
            }
        }
        return result;
    }
}
