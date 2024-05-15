package com.ieltswise.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Support service",
                        email = "ieltswise67.help@gmail.com",
                        url = "https://www.example.com/support"
                ),
                title = "ieltswise67",
                description = "A web platform for learning English.",
                version = "1.0"
        ),
        servers = {
                @Server(
                        description = "LOCAL ENVIRONMENT",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "STAGING ENVIRONMENT",
                        url = "https://178.172.255.18"
                ),
                @Server(
                        description = "PROD ENVIRONMENT",
                        url = " "
                )
        }
)
public class OpenApiConfig {
}
