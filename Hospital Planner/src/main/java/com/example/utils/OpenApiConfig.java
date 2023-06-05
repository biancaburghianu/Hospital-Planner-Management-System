package com.example.utils;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
    static final String SECURITY_SCHEME_NAME = "Bearer Token";
    @Bean
    public OpenAPI usersMicroserviceOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(
                        new Components()
                                .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                        new SecurityScheme()
                                                .name(SECURITY_SCHEME_NAME)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .info(new Info().title("HospitalPlanner")
                        .description("HospitalPlanner API")
                        .version("1.0"));

    }
}
