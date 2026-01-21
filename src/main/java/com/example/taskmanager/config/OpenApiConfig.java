package com.example.taskmanager.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Task Manager API",
        version = "1.0",
        description = "API para gestión de usuarios y tareas",
        contact = @Contact(
            name = "Camilo Villa",
            email = "cami.villa13@hotmail.com"
        )
    )
)
public class OpenApiConfig {

}
