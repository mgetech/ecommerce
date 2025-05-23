package com.example.Product.config;

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
                        .title("Product Service API")
                        .version("1.0")
                        .description("Handles product creation and management"))
                .servers(List.of(
                        new Server().url("https://product-service-716746262210.europe-west3.run.app"), // Cloud
                        new Server().url("http://localhost:8082"),  // Local dev
                        new Server().url("http://product-service:8082")  // Docker dev


                ));
    }
}
