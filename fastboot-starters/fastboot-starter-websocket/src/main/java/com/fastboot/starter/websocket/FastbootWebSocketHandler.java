package com.fastboot.starter.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * 基于 Redis Pub/Sub 的 WebSocket 广播处理器。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public class FastbootWebSocketHandler extends TextWebSocketHandler {

    public static final String TOPIC = "fb:ws:broadcast";

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final RTopic topic;

    public FastbootWebSocketHandler(RedissonClient redissonClient) {
        this.topic = redissonClient.getTopic(TOPIC);
        this.topic.addListener(
                String.class,
                (channel, msg) -> {
                    for (WebSocketSession session : sessions.values()) {
                        if (session.isOpen()) {
                            try {
                                session.sendMessage(new TextMessage(msg));
                            } catch (IOException ignored) {
                                // ignore
                            }
                        }
                    }
                });
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.put(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId());
    }

    public void broadcast(String message) {
        topic.publish(message);
    }
}
