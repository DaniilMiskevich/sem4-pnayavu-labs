package com.daniilmiskevich.labs.docs.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        title = "Reddit Clone REST API docs",
        description = "This project is a backend implementation of a simplified Reddit clone "
            + "using Java 21 and Spring Boot. It serves the purpose of learning how to build "
            + "REST APIs and exploring core Spring Boot features.",
        version = "0.4.0"),
    servers = {
        @Server(url = "http://localhost:8080")
    })
public class OpenApiConfig {
}
