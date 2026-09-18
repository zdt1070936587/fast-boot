# 0001. Modular monolith first

Date: 2026-09-18

## Status

Accepted

## Context

The repository previously advertised monolith and microservice as equal deployment modes, while services shared one database and empty bounded contexts.

## Decision

Default delivery is a modular monolith (`fastboot-boot-monolith`). `fastboot-boot-microservice` is experimental. A service may be split only when it has an independent schema, independent release, and an HTTP/Feign contract.

## Consequences

Local development and production hardening target the monolith. Gateway `lb://` URIs and reserved schemas (`fb_user`, `fb_authz`, `fb_org`) exist for a future split.
