package com.fastboot.starter.test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * 组合注解：Spring Boot 集成测试 + Testcontainers（需配合具体容器基类或自行声明容器）。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootTest
@Testcontainers
@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
public @interface FastbootIntegrationTest {}
