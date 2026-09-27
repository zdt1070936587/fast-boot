# Changelog

## [Unreleased]

### Added

- `fastboot-starter-web/security/mybatis/redis/test` 增量 starter 化
- 各业务模块 `*ModuleAutoConfiguration` 与 `AutoConfiguration.imports`
- `tools/fastboot-codegen`（Picocli + Freemarker）
- `fastboot-cloud` 远程适配骨架（授权 / 租户）
- VitePress 文档站 `website/`
- CI：JDK 17/21 矩阵、Spotless（非阻塞）、UI 构建 job

### Changed

- 默认构建移除 `fastboot-boot-microservice`（`-Pcloud` 启用）
- 删除空模块 `fastboot-application`，业务模块不再依赖该 artifact

### Removed

- `fastboot-ddd/fastboot-application` 占位模块
