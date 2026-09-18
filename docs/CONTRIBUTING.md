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
