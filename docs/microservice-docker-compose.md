# 微服务本地启动（Docker Compose）

默认 `mvn compile` **不包含** `fastboot-boot-microservice`，需启用 Maven profile `cloud`：

```bash
mvn -Pcloud -pl fastboot-boot-microservice -am package
```

## 基础设施

1. 启动 PostgreSQL 与 Redis：

```bash
docker compose -f docker-compose.yml up -d
```

2. （可选）Nacos 注册中心：

```bash
docker compose -f docker-compose.nacos.yml up -d
```

3. 全栈参考清单见 `docker-compose.all.yml`（含 gateway / nacos 注释说明）。

## 服务端口（示例）

| 服务 | 默认端口 | 说明 |
|------|----------|------|
| Gateway | 18080 | 统一入口 |
| User | 18081 | 用户域 |
| Authz | 18082 | 授权域 |
| Org | 18083 | 组织域 |

配置项示例：

- `fastboot.authz.base-url` — user/org 服务调用授权 Feign
- `fastboot.remote.authz.enabled=true` — 启用 `fastboot-cloud` 内远程授权适配器
- `fastboot.remote.tenant.enabled=true` — 启用租户远程查询占位实现

## 单体对照

模块化单体仍使用 `fastboot-boot-monolith` + `docker-compose.app.yml`，无需 Nacos。
