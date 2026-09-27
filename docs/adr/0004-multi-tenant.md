# 0004. Multi-tenant context and SQL line

Date: 2026-09-25

## Status

Accepted

## Context

SaaS deployments need row-level tenant isolation without scattering `tenant_id` checks in every repository. Login must resolve tenant from header/domain and validate against the tenant registry.

## Decision

1. Introduce `fastboot-starter-tenant` with `TenantContext` (ThreadLocal), `TenantResolver`, and MyBatis-Plus `TenantLineInnerInterceptor` (order 1 in the plugin chain).
2. Redis keys and permission caches use `TenantKeyPrefix` / `t:{tenantId}:` when a tenant is present.
3. `@TenantIgnore` on methods or classes disables tenant SQL predicates via `TenantIgnoreAspect` for cross-tenant maintenance jobs.
4. `TenantQueryPort` (system-api) validates tenant status during login; default tenant id `1` when no resolver is configured.

## Consequences

All business tables carry `tenant_id` (Flyway `V4__tenant.sql`). New mappers inherit tenant filtering automatically. Microservice splits must propagate tenant id on HTTP and messaging.
