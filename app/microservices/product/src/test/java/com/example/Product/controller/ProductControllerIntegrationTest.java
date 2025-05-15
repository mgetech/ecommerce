package com.example.Product.controller;


import com.example.Product.dto.ProductRequestDTO;
import com.example.Product.dto.ProductResponseDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;


import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static Long createdId;

    private String baseUrl() {
        return "http://localhost:" + port + "/products";
    }

    @Test
    @Order(1)
    void testCreateProduct() {
        ProductRequestDTO request = new ProductRequestDTO("Keyboard", "Mechanical keyboard", 49.99);
        ResponseEntity<ProductResponseDTO> response = restTemplate.postForEntity(baseUrl(), request, ProductResponseDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        createdId = response.getBody().id();
    }

    @Test
    @Order(2)
    void testGetProduct() {
        ResponseEntity<ProductResponseDTO> response = restTemplate.getForEntity(baseUrl() + "/" + createdId, ProductResponseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Keyboard", response.getBody().name());
    }

    @Test
    @Order(3)
    void testUpdateProduct() {
        ProductRequestDTO update = new ProductRequestDTO("Gaming Keyboard", "RGB lights", 59.99);
        HttpEntity<ProductRequestDTO> entity = new HttpEntity<>(update);

        ResponseEntity<ProductResponseDTO> response = restTemplate.exchange(
                baseUrl() + "/" + createdId,
                HttpMethod.PUT,
                entity,
                ProductResponseDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Gaming Keyboard", response.getBody().name());
    }

    @Test
    @Order(4)
    void testDeleteProduct() {
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl() + "/" + createdId,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @Order(5)
    void testGetDeletedProduct() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl() + "/" + createdId, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}