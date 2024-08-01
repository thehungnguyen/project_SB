package com.hungnt.project_SB.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                // Thông tin chi tiết cho API
                .info(new Info()
                        .title("API Document")
                        .version("v1.0")
                        .description("API Service")
                )
                // Server có thể truy cập
                .servers(List.of(
                        new Server().url("http://localhost:8888").description("Local Server")
                ))
                // Thiết lập bảo mật cho API
                .components(
                        new Components().addSecuritySchemes(
                                        "bearerAuth", new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                        )
                )
                .security(List.of(
                        new SecurityRequirement().addList("bearerAuth")
                ));
    }

    @Bean
    public GroupedOpenApi groupedOpenApi(){
        return GroupedOpenApi.builder()
                .group("API Group 1")
                .packagesToScan("com.hungnt.project_SB.controller")
                .build();
    }
}
