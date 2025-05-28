package com.example.user.controller;

import com.example.user.dto.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    private TestRestTemplate restTemplate;


    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/users";
    }

    @Test
    @Order(1)
    void testCreateUser() {
        UserRequestDTO request = new UserRequestDTO("John Doe", "john.doe@example.com");

        ResponseEntity<UserResponseDTO> response = restTemplate.postForEntity(baseUrl, request, UserResponseDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().email()).isEqualTo("john.doe@example.com");
    }

    @Test
    @Order(2)
    void testGetUserById() {
        UserRequestDTO request = new UserRequestDTO("Jane Smith", "jane.smith@example.com");
        UserResponseDTO created = restTemplate.postForEntity(baseUrl, request, UserResponseDTO.class).getBody();

        ResponseEntity<UserResponseDTO> response =
                restTemplate.getForEntity(baseUrl + "/" + Objects.requireNonNull(created).id(), UserResponseDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().name()).isEqualTo("Jane Smith");
    }

    @Test
    @Order(3)
    void testInvalidEmailShouldFail() {
        UserRequestDTO badRequest = new UserRequestDTO("Invalid user", "bad-email");

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, badRequest, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Invalid email format");
    }

    @Test
    @Order(4)
    void testGetNonExistentUser() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/999999", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("user not found");
    }


    @Test
    @Order(5)
    void updateUser() {
        UserRequestDTO updatedUser = new UserRequestDTO("John Updated", "updated.john@gmail.com");
        HttpEntity<UserRequestDTO> requestEntity = new HttpEntity<>(updatedUser);

        ResponseEntity<UserResponseDTO> response = restTemplate.exchange(
                baseUrl + "/" + 3,
                HttpMethod.PUT,
                requestEntity,
                UserResponseDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John Updated", response.getBody().name());
        assertEquals("updated.john@gmail.com", response.getBody().email());
    }


    @Test
    @Order(6)
    void testDeleteUser() {
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + 3,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}

