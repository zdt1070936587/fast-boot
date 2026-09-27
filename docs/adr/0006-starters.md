# 0006. FastBoot starters layout

Date: 2026-09-25

## Status

Accepted

## Context

Cross-cutting capabilities (web envelope, security, MyBatis, Redis, protection, tenant, data permission, Excel, WebSocket, test fixtures) were embedded in `fastboot-infrastructure` or duplicated across modules. We need optional, composable Spring Boot auto-configurations with versions centralized in `fastboot-dependencies`.

## Decision

1. `fastboot-starters` is an aggregator POM listing one jar per capability (`fastboot-starter-web`, `-security`, `-mybatis`, `-redis`, `-protection`, `-tenant`, `-data-permission`, `-excel`, `-websocket`, `-test`, plus optional oauth/captcha/passkey).
2. `fastboot-dependencies` BOM publishes every starter and `*-api` module version for consistent `dependencyManagement`.
3. `fastboot-boot-monolith` pulls the default starter set; feature modules depend only on the starters they need (often `optional` for annotations).
4. Core MyBatis plugin ordering remains in `fastboot-infrastructure` `MybatisPlusConfig`; starters register ordered `InnerInterceptor` beans only.

## Consequences

New applications compose starters instead of copying configuration. Experimental starters (oauth2, passkey) can ship without bloating the monolith classpath.
