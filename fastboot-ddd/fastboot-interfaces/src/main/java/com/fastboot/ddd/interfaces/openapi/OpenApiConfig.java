package com.fastboot.ddd.interfaces.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 3 文档配置，启用 Bearer JWT 安全方案。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Configuration
@ConditionalOnProperty(name = "fastboot.security.permit-docs", havingValue = "true")
public class OpenApiConfig {

  /** 构建带 Bearer 认证的 OpenAPI 元数据 */
  @Bean
  public OpenAPI fastbootOpenApi() {
    SecurityScheme bearer =
        new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT");
    return new OpenAPI()
        .info(new Info().title("fast-boot API").version("v1"))
        .components(new Components().addSecuritySchemes("bearerAuth", bearer))
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
  }
}
