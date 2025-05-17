package com.example.order.config;

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
                        .title("Order Service API")
                        .version("1.0")
                        .description("Handles order placement and management"))
                .servers(List.of(
                        new Server().url("https://order-service-716746262210.europe-west3.run.app"), // Could
                        new Server().url("http://localhost:8083"),  // Local dev
                        new Server().url("http://order-service:8083")  // Docer dev


                ));
    }
}
