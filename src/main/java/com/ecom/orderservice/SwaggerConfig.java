package com.ecom.orderservice;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;

@Configuration
public class SwaggerConfig {
        @Bean
        public GroupedOpenApi publicApi() {
                return GroupedOpenApi.builder()
                                .group("public")
                                .pathsToMatch("/**")
                                .build();
        }

        @Bean
        public OpenAPI openAPI() {
                return new OpenAPI()
                                .servers(List.of(
                                                new Server()
                                                                .url("https://ecom-order-service-934927433282.us-central1.run.app")))
                                .info(new Info()
                                                .title("ECOM-Order-Service")
                                                .description(
                                                                "This service manages order")
                                                .version("1.0.0")
                                                .contact(new Contact()
                                                                .url("https://github.com/CODERonak")))
                                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                                .components(new Components()
                                                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                                                .name("bearerAuth")
                                                                .type(SecurityScheme.Type.HTTP)
                                                                .scheme("bearer")
                                                                .bearerFormat("JWT")));
        }
}
