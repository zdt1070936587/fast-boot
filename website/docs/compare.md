# 与常见脚手架对比

| 维度 | FastBoot | pig / ruoyi | JeecgBoot | dante-cloud | matecloud |
|------|----------|-------------|-----------|-------------|-----------|
| 默认形态 | 模块化单体 | 微服务 / 单体可选 | 低代码单体 | 微服务 | 微服务 |
| 领域建模 | DDD 模块 + 端口 | 经典三层 | 在线表单驱动 | DDD + 云原生 | 分层 + 云 |
| 权限 | Sa-Token + authz 模块 | Spring Security / 内置 | Shiro / 内置 | OAuth2 体系 | 内置 |
| 数据库 | PostgreSQL 优先 | MySQL 常见 | MySQL | 多数据库 | MySQL |
| 扩展方式 | modules + starters + codegen | 代码生成 / 模块 | 在线配置 | 服务拆分 | 组件化 |
| 微服务 | `-Pcloud` 实验路径 | 一等公民 | 可选 | 一等公民 | 一等公民 |

FastBoot 定位：**先模块化单体落地，再按 authz-api 等契约渐进拆分**，避免一开始就维护全量微服务拓扑。
