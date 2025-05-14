package com.example.order.controller;

import com.example.order.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.*;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;
import org.wiremock.spring.InjectWireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableWireMock({
        @ConfigureWireMock(name = "user-service", port = 8081),
        @ConfigureWireMock(name = "product-service", port = 8082)
})
@Transactional
class OrderControllerIntegrationTest {

    @InjectWireMock("user-service")
    private WireMockServer userServiceMock;

    @InjectWireMock("product-service")
    private WireMockServer productServiceMock;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testCreateOrder() {
        userServiceMock.stubFor(get(urlEqualTo("/users/3"))
                .willReturn(okJson("{\"id\":3,\"name\":\"John\"}")));

        productServiceMock.stubFor(get(urlEqualTo("/products/3"))
                .willReturn(okJson("{\"id\":3,\"name\":\"Phone\"}")));

        OrderRequestDTO dto = new OrderRequestDTO(3L, 3L, 5);

        webTestClient.post()
                .uri("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class)
                .value(body -> {
                    Assertions.assertTrue(body.contains("PENDING"));
                });
    }
}
