package com.fastboot.modules.infra.api;

import java.util.Map;

/**
 * 通知发送端口。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public interface NotifyPort {

    void send(String channel, String templateCode, String receiver, Map<String, Object> params);
}
