package com.myoffice.app.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfigurations {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList("Bearer Authentication")
                )
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .bearerFormat("JWT")
                                .scheme("bearer")
                        )
                )
                .info(new Info()
                        .title("MyOffice API Doc")
                        .description("Online office for everyone")
                        .version("1.0")
                        .contact(new Contact()
                                .name("luzemin")
                                .email("592640424@qq.com")
                                .url("https://www.cnblogs.com/talentzemin")
                        )
                        .license(new License()
                                .name("License of API")
                                .url("https://www.cnblogs.com/talentzemin")
                        )
                );
    }

    @Bean("defaultGroupApi")
    public GroupedOpenApi defaultGroupApi() {
        return GroupedOpenApi.builder()
                .group("Default")
                .pathsToExclude("/api/admin/**", "/api/auth/admin/**")
                .build();
    }

    @Bean("adminGroupApi")
    public GroupedOpenApi adminGroupApi() {
        return GroupedOpenApi.builder()
                .group("Admin")
                .pathsToMatch("/api/admin/**", "/api/auth/admin/**")
                .build();
    }
}