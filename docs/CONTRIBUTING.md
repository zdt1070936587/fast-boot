# Contributing

## Default path

Use the modular monolith. Do not add cross-service table access.

## Layout

- `fastboot-common`: error codes, `R`, annotations
- `fastboot-ddd`: kernel only
- `fastboot-modules-*`: bounded contexts
- `fastboot-boot-monolith`: supported runtime

## Rules

1. New tables go through Flyway (`V{n}__*.sql` in `fastboot-infrastructure`).
2. Domain classes stay free of Spring.
3. Controllers return `R<DTO>` records, never `Map`.
4. Secrets come from environment variables.
5. `mvn verify` must pass (Testcontainers needs Docker).
6. Cross-module calls must go through `*-api` contracts (currently `fastboot-modules-authz-api`); never depend on another module's `application` / `infrastructure`.
7. `@OperationLog` must explicitly declare `args` SpEL expressions; never rely on dumping raw method arguments (passwords must not appear in logs).
8. Side effects that invalidate caches after DB writes belong in `@TransactionalEventListener(AFTER_COMMIT)`.
