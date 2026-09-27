# Security Policy

## Supported versions

Security fixes are applied to the `master` branch and the latest `0.1.x` snapshot used by `fastboot-boot-monolith`.

## Reporting a vulnerability

Please **do not** open public GitHub issues for security problems.

Email maintainers with:

- Description and impact
- Steps to reproduce
- Affected module or endpoint
- Suggested fix (if any)

We aim to acknowledge reports within five business days.

## Secure defaults

- Change default passwords (`FASTBOOT_ADMIN_PASSWORD`, database, Redis) before production.
- Enable HTTPS and configure trusted reverse-proxy headers for client IP used in login rate limiting.
- Restrict `/api/v1/internal/**` with `X-Internal-Token` when `fastboot.security.permit-internal=true`.
