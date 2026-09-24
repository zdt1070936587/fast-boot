package com.fastboot.boot.monolith;

import static com.tngtech.archunit.base.DescribedPredicate.alwaysTrue;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "com.fastboot.modules", importOptions = ImportOption.DoNotIncludeTests.class)
class ModuleArchitectureTest {

    @ArchTest
    static final ArchRule domainMustNotDependOnSpring =
            noClasses()
                    .that()
                    .resideInAPackage("..domain..")
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage("org.springframework..", "com.baomidou..", "cn.dev33.satoken..");

    @ArchTest
    static final ArchRule domainMustNotDependOnMybatisMapper =
            noClasses()
                    .that()
                    .resideInAPackage("..domain..")
                    .should()
                    .dependOnClassesThat()
                    .resideInAPackage("..infrastructure.mapper..");

    @ArchTest
    static final ArchRule modulesShouldOnlyCoupleViaAuthzApi =
            slices()
                    .matching("com.fastboot.modules.(*)..")
                    .should()
                    .notDependOnEachOther()
                    .ignoreDependency(alwaysTrue(), resideInAPackage("..modules.authz.api.."));

    @ArchTest
    static final ArchRule apiPackagesShouldStayPure =
            classes()
                    .that()
                    .resideInAPackage("..modules.*.api..")
                    .should()
                    .onlyDependOnClassesThat()
                    .resideInAnyPackage(
                            "java..",
                            "javax..",
                            "jakarta..",
                            "..modules.*.api..",
                            "com.fastboot.common..");

    @ArchTest
    static final ArchRule applicationMustNotDependOnInterfaces =
            noClasses()
                    .that()
                    .resideInAPackage("..application..")
                    .should()
                    .dependOnClassesThat()
                    .resideInAPackage("..interfaces..");

    @ArchTest
    static final ArchRule userAndOrgApplicationMustNotDependOnMapper =
            noClasses()
                    .that()
                    .resideInAnyPackage("..modules.user.application..", "..modules.org.application..")
                    .should()
                    .dependOnClassesThat()
                    .resideInAPackage("..infrastructure.mapper..");
}
