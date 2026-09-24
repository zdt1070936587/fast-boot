# 0003. Module API contracts and AFTER_COMMIT side effects

Date: 2026-09-24

## Status

Accepted

## Context

Cross-module calls previously depended on application implementations (`AuthzAppService`) and the full `authz` jar. Permission cache eviction ran inside transactions, allowing dirty cache refill before commit. Internal endpoints lacked a service-to-service credential.

## Decision

1. Introduce `fastboot-modules-authz-api` holding `PermissionQueryPort`, `DataScopePort`, and transfer DTOs. Other modules may depend only on this API jar.
2. Publish `PermissionsChanged` from application services; evict permission cache in `@TransactionalEventListener(phase = AFTER_COMMIT, fallbackExecution = true)`.
3. Protect `/api/v1/internal/**` with `X-Internal-Token` when `fastboot.security.permit-internal=true`.
4. View DTOs live under `application.dto`; request DTOs stay under `interfaces.dto`. Application must not depend on `interfaces`.

## Consequences

ArchUnit enforces module isolation (via `authz.api`), API purity, and application→interfaces prohibition. Microservice user process binds `PermissionQueryPort` to a Feign adapter instead of scanning `modules.authz`.
