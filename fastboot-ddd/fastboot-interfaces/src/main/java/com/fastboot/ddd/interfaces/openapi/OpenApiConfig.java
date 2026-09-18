package com.fastboot.ddd.interfaces.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "fastboot.security.permit-docs", havingValue = "true")
public class OpenApiConfig {

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
