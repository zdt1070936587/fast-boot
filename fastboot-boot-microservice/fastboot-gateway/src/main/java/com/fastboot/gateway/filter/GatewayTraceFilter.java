package com.fastboot.gateway.filter;

import com.fastboot.common.api.TraceIdHolder;
import java.util.UUID;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关全局链路追踪过滤器，透传或生成 TraceId 并写入请求与响应头。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Component
public class GatewayTraceFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String traceId = exchange.getRequest().getHeaders().getFirst(TraceIdHolder.HEADER);
        if (traceId == null || traceId.isBlank()) {
            traceId = UUID.randomUUID().toString().replace("-", "");
        }
        String id = traceId;
        ServerHttpRequest request =
                exchange.getRequest().mutate().header(TraceIdHolder.HEADER, id).build();
        exchange.getResponse().getHeaders().set(TraceIdHolder.HEADER, id);
        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return -200;
    }
}
