# 0002. Hexagonal modules

Date: 2026-09-18

## Status

Accepted

## Context

`fastboot-ddd` was an empty horizontal slice. Business code lived beside unused domain objects.

## Decision

`fastboot-ddd` is a thin kernel (aggregate base, MyBatis, web error handling). Business code lives in `fastboot-modules-*` with `domain` / `application` / `infrastructure` / `interfaces`. Domain must not depend on Spring, MyBatis, or Sa-Token. Application talks to repositories, not mappers.

## Consequences

ArchUnit in the monolith module enforces domain isolation.
