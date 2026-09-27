# FastBoot Codegen

基于 **Picocli + Freemarker** 的模块与聚合脚手架，Maven 模块位于 `tools/fastboot-codegen`。

## 构建

```bash
mvn -pl tools/fastboot-codegen -am package
java -jar tools/fastboot-codegen/target/fastboot-codegen-0.1.0-SNAPSHOT.jar --help
```

## 命令

### `new-module`

在 `fastboot-modules/` 下生成 `fastboot-modules-<name>` 骨架（pom、模块 AutoConfiguration、`AutoConfiguration.imports`）。

```bash
java -jar tools/fastboot-codegen/target/fastboot-codegen-0.1.0-SNAPSHOT.jar new-module --name order --root .
```

生成后手动：

1. 在 `fastboot-modules/pom.xml` 与 `fastboot-dependencies/pom.xml` 登记模块
2. 在 `fastboot-boot-monolith/pom.xml` 引入依赖（若需单体加载）

### `new-aggregate`

按 YAML 规格生成 domain / application / infrastructure / interfaces、Flyway 片段、简单测试与 `http/` 文件。

```bash
java -jar tools/fastboot-codegen/target/fastboot-codegen-0.1.0-SNAPSHOT.jar \
  new-aggregate --module order --spec tools/fastboot-codegen/sample.yml --root .
```

## YAML 规格

| 字段 | 说明 |
|------|------|
| `aggregate` | 聚合类名（PascalCase） |
| `table` | 数据库表名 |
| `description` | 类注释 |

示例见 `tools/fastboot-codegen/sample.yml`。

## 与手工模板的关系

历史目录 `tools/module-template` 仍可作为参考；新模块优先使用 `new-module` + `new-aggregate`。
