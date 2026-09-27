# 快速开始

## 环境

- JDK 17+
- Maven 3.6+
- Docker（PostgreSQL / Redis）

## 单体运行

```bash
docker compose up -d
mvn -pl fastboot-boot-monolith -am spring-boot:run
```

默认端口 `8080`，健康检查 `/actuator/health`。

## 代码生成

```bash
mvn -pl tools/fastboot-codegen -am package
java -jar tools/fastboot-codegen/target/fastboot-codegen-0.1.0-SNAPSHOT.jar new-module --name order
```

详见 [docs/codegen.md](../docs/codegen.md)。

## 微服务（可选）

```bash
mvn -Pcloud -pl fastboot-boot-microservice -am package
```

参见 [docs/microservice-docker-compose.md](../docs/microservice-docker-compose.md)。
