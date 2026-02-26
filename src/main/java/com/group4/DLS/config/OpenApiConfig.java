package com.group4.DLS.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Data Labeling System (DLS) API",
        version = "1.0.0",
        description = "Backend API for managing data annotation projects, assignments, datasets, and labeling workflows",
        contact = @Contact(
            name = "Group 4",
            email = "support@dls.com"
        )
    ),
    servers = {
        @Server(
            description = "Development Server",
            url = "http://localhost:8081/"
        ),
        @Server(
            description = "Prod Server",
            url = "https://dls-beta.hikarimoon.pro/"
        )
    }
)
@SecurityScheme(
    name = "Bearer Authentication",
    description = "JWT authentication token. Login to get your token.",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
    // Configuration is handled by annotations
}
