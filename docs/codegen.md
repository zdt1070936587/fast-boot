# Module template

Copy `tools/module-template` into `fastboot-modules/fastboot-modules-<name>` then:

1. Add the module to `fastboot-modules/pom.xml` and `fastboot-dependencies/pom.xml`
2. Replace `__NAME__` / `__package__`
3. Add the dependency to `fastboot-boot-monolith`
4. Put Flyway scripts in infrastructure, not in the module

Packages:

- `__package__.domain`
- `__package__.application`
- `__package__.infrastructure`
- `__package__.interfaces`
