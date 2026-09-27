package com.fastboot.modules.infra.application.notify;

/**
 * 通知渠道 SPI。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public interface NotifyChannel {

    String channel();

    void send(String receiver, String title, String content);
}
