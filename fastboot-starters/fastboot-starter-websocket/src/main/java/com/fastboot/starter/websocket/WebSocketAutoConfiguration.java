package com.fastboot.starter.websocket;

import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebSocket 自动配置。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@AutoConfiguration
@EnableWebSocket
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class WebSocketAutoConfiguration {

    @Bean
    @ConditionalOnBean(RedissonClient.class)
    @ConditionalOnMissingBean
    public FastbootWebSocketHandler fastbootWebSocketHandler(RedissonClient redissonClient) {
        return new FastbootWebSocketHandler(redissonClient);
    }

    @Bean
    @ConditionalOnBean(FastbootWebSocketHandler.class)
    public WebSocketConfigurer fastbootWebSocketConfigurer(FastbootWebSocketHandler handler) {
        return new WebSocketConfigurer() {
            @Override
            public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
                registry
                        .addHandler(handler, "/ws")
                        .addInterceptors(new SaTokenHandshakeInterceptor())
                        .setAllowedOrigins("*");
            }
        };
    }
}
