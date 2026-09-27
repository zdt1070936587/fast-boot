# fast-boot

基于 **Spring Boot 3.4**、**JDK 21+**、**MyBatis-Plus**、**Sa-Token**、**PostgreSQL**、**Redis / Redisson** 的模块化后端脚手架。

**默认交付形态是模块化单体**（`fastboot-boot-monolith`）。`fastboot-boot-microservice` 仅作实验，不作为生产默认路径。

## 环境要求

- JDK **17+**（推荐 21；开启虚拟线程请使用 21 并设置 `VIRTUAL_THREADS=true`）
- Maven 3.6+
- Docker（本地 Postgres / Redis，以及 Testcontainers）

## 本地基础设施

```bash
docker compose up -d
```

- PostgreSQL：`127.0.0.1:15432`，库 `fastboot`，用户/密码默认 `dafu` / `123456`
- Redis：`localhost:6379`，默认密码 `fastboot`

可选 Nacos：

```bash
docker compose -f docker-compose.yml -f docker-compose.nacos.yml up -d
```

复制 `.env.example` 为 `.env` 后按需修改。生产环境不要使用文档中的默认口令。

## 构建与测试

```bash
mvn -DskipTests compile   # 默认 reactor（不含 microservice）
mvn verify                # 含 Testcontainers 集成测试
```

微服务聚合模块需显式启用 profile：

```bash
mvn -Pcloud -pl fastboot-boot-microservice -am package
```

引入 Spring Cloud Alibaba BOM（实验性微服务发现）：

```bash
mvn -Pwith-cloud-alibaba verify
```

一键本地数据层（Postgres + Redis，见 `docker-compose.all.yml`）：

```bash
docker compose -f docker-compose.all.yml up -d
```

## 单体模式（推荐）

```bash
cd fastboot-boot-monolith
mvn spring-boot:run
```

或运行 `com.fastboot.boot.monolith.FastbootMonolithApplication`。

默认 `spring.profiles.active=local`：

- 应用端口 **8080**，Actuator **8081**（`/actuator/health`）
- Flyway 执行 `fastboot-infrastructure` 中的 `db/migration`
- local 会为 `admin` 写入 BCrypt 密码（`FASTBOOT_ADMIN_PASSWORD`，默认 `admin`）
- OpenAPI：`/swagger-ui/index.html`（仅 local/dev）

### 常用接口（`/api/v1`）

| 说明 | 方法 | 路径 |
| --- | --- | --- |
| 登录 | POST | `/api/v1/auth/login` `{"username":"admin","password":"admin"}` |
| 当前用户 | GET | `/api/v1/auth/info` |
| 当前菜单 | GET | `/api/v1/auth/menus` |
| 用户 CRUD | GET/POST/PUT/DELETE | `/api/v1/users` |
| 权限示例 | GET | `/api/v1/demo/ping`（`sys:demo:read`） |
| 角色 | GET | `/api/v1/authz/roles` |
| 组织 | GET | `/api/v1/org` |
| 健康 | GET | `http://localhost:8081/actuator/health` |

Token 请求头：`Authorization: Bearer <tokenValue>`。

HTTP 示例见 `http/api.http`、`http/system.http`、`http/infra.http`。

## 微服务模式（experimental）

聚合模块：`fastboot-boot-microservice`。当前仍共享同一 `fastboot` 库；`V3` 预留了 `fb_user` / `fb_authz` / `fb_org` schema，真正分库前不要当生产用。

网关默认静态 URI，可通过环境变量改为 `lb://fastboot-xxx-service`（需 Nacos + LoadBalancer）。网关会校验 Redis 中的 Sa-Token，并拒绝转发 `/api/v1/internal/**`。

## 模块一览

| 模块 | 作用 |
| --- | --- |
| `fastboot-dependencies` | 唯一第三方/内部版本 BOM |
| `fastboot-common` | `R`、错误码、锁/审计/幂等/限流注解 |
| `fastboot-ddd/*` | 内核：聚合基类、MyBatis、Web 异常与 TraceId |
| `fastboot-starters/*` | 可组合自动配置：web、security、mybatis、redis、protection、tenant、data-permission、excel、websocket、test、oauth2、justauth、captcha、passkey |
| `fastboot-modules-authz-api` | 授权契约（权限/数据范围/`MenuQueryPort`） |
| `fastboot-modules-user-api` | 用户查询契约（`UserQueryPort`） |
| `fastboot-modules-system-api` | 系统契约（租户/字典/配置/审计端口） |
| `fastboot-modules-infra-api` | 基础设施契约（文件/通知/任务端口） |
| `fastboot-modules-bpm-api` / `fastboot-modules-bpm` | 工作流契约与占位实现（后续接入 Flowable 等） |
| `fastboot-modules-ai-api` / `fastboot-modules-ai` | AI 契约与占位实现 |
| `fastboot-modules-user` | 认证、`/auth/menus`、用户 CRUD |
| `fastboot-modules-authz` | 角色/权限/菜单管理与查询实现 |
| `fastboot-modules-org` | 组织树；`OrgAppService#list` 带 `@DataPermission` |
| `fastboot-modules-system` | 租户、字典、配置、审计、在线用户 |
| `fastboot-modules-infra` | 文件、任务、通知、访问日志、站内信 |
| `fastboot-cloud` | 单体/微服务切换时的 Feign 远程端口适配 |
| `fastboot-boot-monolith` | 默认启动器 |
| `fastboot-boot-microservice/*` | 实验性拆分（`-Pcloud`） |
| `tools/fastboot-codegen` | 新模块脚手架（`-Ptools`） |
| `fastboot-ui` | Vue 3 管理端（Vite + UnoCSS） |
| `deploy/` | K8s 单体部署与 Prometheus/Grafana 参考 |

分层约定见 `docs/architecture.md`、`docs/adr` 与 `docs/CONTRIBUTING.md`。新模块模板见 `docs/codegen.md`。

## 应用镜像

先 `mvn -pl fastboot-boot-monolith -am package`，再：

```bash
docker compose -f docker-compose.yml -f docker-compose.app.yml up -d --build
```
