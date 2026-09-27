package com.fastboot.modules.infra.application.notify;

import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * 邮件通知渠道。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Component
public class MailChannel implements NotifyChannel {

    public static final String CHANNEL = "MAIL";

    private final ObjectProvider<JavaMailSender> mailSender;

    public MailChannel(ObjectProvider<JavaMailSender> mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public String channel() {
        return CHANNEL;
    }

    @Override
    public void send(String receiver, String title, String content) {
        JavaMailSender sender = mailSender.getIfAvailable();
        if (sender == null) {
            throw new BizException(ErrorCode.INTERNAL_ERROR, "邮件服务未配置");
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(receiver);
        message.setSubject(title == null ? "" : title);
        message.setText(content == null ? "" : content);
        sender.send(message);
    }
}
