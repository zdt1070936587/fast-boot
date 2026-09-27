# 可观测性

## 本地栈

```bash
docker compose -f docker-compose.observability.yml up -d
```

- Zipkin UI：`http://localhost:9411`
- Prometheus：`http://localhost:9090`
- Grafana：`http://localhost:3000`（admin / admin）

## 应用配置

`fastboot-boot-monolith` 的 `application.yml` 已包含 tracing 采样与 Zipkin 端点占位：

- `TRACING_SAMPLE`：采样率（默认 `0.1`）
- `ZIPKIN_ENDPOINT`：Span 上报地址

日志 pattern 输出 `traceId` / `spanId`（需启用 Micrometer Tracing 依赖后生效）。

## Prometheus

管理端口（默认 `8081`）暴露 `prometheus` 端点；`deploy/prometheus/prometheus.yml` 示例抓取 monolith。

## Grafana

导入 `deploy/grafana/dashboard-fastboot.json` 作为起步看板。
