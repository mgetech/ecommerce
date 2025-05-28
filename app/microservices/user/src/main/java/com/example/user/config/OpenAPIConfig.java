package com.example.user.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("user Service API")
                        .version("1.0")
                        .description("Handles user registration and management"))
                .servers(List.of(
                        new Server().url("https://user-service-716746262210.europe-west3.run.app"), // Cloud
                        new Server().url("http://localhost:8081"),  // Local dev
                        new Server().url("http://user-service:8081")  // Docker dev


                ));
    }
}
