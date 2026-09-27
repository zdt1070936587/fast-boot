# 0005. SQL-level data permission

Date: 2026-09-25

## Status

Accepted

## Context

Organization-scoped RBAC needs more than API permission codes: list queries must not leak rows outside the user's data scope (self, dept, custom org set, or all).

## Decision

1. `fastboot-starter-data-permission` registers `FastbootDataPermissionInterceptor` (MyBatis order 2) and `@DataPermission` AOP to toggle rules per service method.
2. Rules (`OrgDataPermissionRule`, `SelfDataPermissionRule`) consume `DataScopePort` from `fastboot-modules-authz-api`.
3. Application services may still filter in memory for backward compatibility (e.g. `OrgAppService.list` keeps `DataScopePort` filtering alongside `@DataPermission`).

## Consequences

Data scope changes require cache eviction on role assignment. ArchUnit allows cross-module coupling only through `..modules..api..` packages.
