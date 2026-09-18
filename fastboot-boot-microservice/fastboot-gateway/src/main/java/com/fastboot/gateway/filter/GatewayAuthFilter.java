package com.fastboot.gateway.filter;

import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关全局认证过滤器，校验登录 Token 并对受保护路径注入转发头。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Component
public class GatewayAuthFilter implements GlobalFilter, Ordered {

  /** 无需登录即可访问的路径前缀列表 */
  private static final List<String> PUBLIC_PREFIXES =
      List.of("/api/v1/auth/login", "/actuator", "/v3/api-docs", "/swagger-ui");

  /** Redis 模板，用于校验 Token 是否在登录态缓存中存在 */
  private final StringRedisTemplate redisTemplate;

  public GatewayAuthFilter(StringRedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String path = exchange.getRequest().getURI().getPath();
    if (isPublic(path)) {
      return chain.filter(exchange);
    }
    String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    String token = extract(header);
    if (token == null || !Boolean.TRUE.equals(redisTemplate.hasKey("Authorization:login:token:" + token))) {
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      byte[] body = "{\"code\":401000,\"message\":\"未登录或登录已失效\"}".getBytes(StandardCharsets.UTF_8);
      return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(body)));
    }
    ServerHttpRequest mutated =
        exchange.getRequest().mutate().header("X-Forwarded-User-Token", token).build();
    return chain.filter(exchange.mutate().request(mutated).build());
  }

  private boolean isPublic(String path) {
    return PUBLIC_PREFIXES.stream().anyMatch(path::startsWith);
  }

  private String extract(String header) {
    if (header == null || header.isBlank()) {
      return null;
    }
    if (header.regionMatches(true, 0, "Bearer ", 0, 7)) {
      return header.substring(7).trim();
    }
    return header.trim();
  }

  @Override
  public int getOrder() {
    return -100;
  }
}
