# 架构摘要

完整说明见仓库根目录 [docs/architecture.md](https://github.com/fastboot/fast-boot/blob/master/docs/architecture.md)（本地路径：`../docs/architecture.md`）。

## 要点

- **默认运行时**：`fastboot-boot-monolith` 扫描 `com.fastboot`，连接 PostgreSQL 与 Redis。
- **DDD 分层**：`fastboot-ddd` 保留 domain / infrastructure / interfaces；业务在 `fastboot-modules-*`。
- **跨模块契约**：如 `fastboot-modules-authz-api` 的 `PermissionQueryPort`、`DataScopePort`。
- **实验微服务**：Gateway + user / authz / org；user 经 Feign 调 authz 内部 API。
- **构建**：默认 `mvn compile` 不含 microservice；`-Pcloud` 加入 `fastboot-boot-microservice`。

## 模块自动装配

各业务模块提供 `*ModuleAutoConfiguration`（默认 `fastboot.modules.<name>.auto-config=false`），便于将来收窄 `scanBasePackages`。
