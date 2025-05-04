package com.example.order.service;
import com.example.order.dto.OrderRequestDTO;
import com.example.order.dto.OrderResponseDTO;
import com.example.order.entity.OrderEntity;
import com.example.order.entity.OrderStatus;
import com.example.order.exception.ExternalServiceException;
import com.example.order.repository.OrderRepository;
import com.example.order.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderServiceTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private WebClient.Builder webClientBuilder;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        when(webClientBuilder.build()).thenReturn(webClient);

    }

    @Test
    @Order(1)
    void createOrder() {
        OrderRequestDTO dto = new OrderRequestDTO(4L, 4L, 1);

        // builder → webClient
        when(webClientBuilder.build()).thenReturn(webClient);

        // both onStatus(...) calls should return the same mock so .bodyToMono() can run
        doReturn(responseSpec).when(responseSpec).onStatus(any(), any());

        // stub user‑service call
        doReturn(requestHeadersUriSpec).when(webClient).get();
        doReturn(requestHeadersSpec)
                .when(requestHeadersUriSpec)
                .uri(anyString(), ArgumentMatchers.<Object>any());   // ← varargs stub
        doReturn(responseSpec).when(requestHeadersSpec).retrieve();
        doReturn(Mono.just("User found"))
                .when(responseSpec).bodyToMono(String.class);

        // stub product‑service call the same way
        doReturn(requestHeadersUriSpec).when(webClient).get();
        doReturn(requestHeadersSpec)
                .when(requestHeadersUriSpec)
                .uri(anyString(), ArgumentMatchers.<Object>any());
        doReturn(responseSpec).when(requestHeadersSpec).retrieve();
        doReturn(Mono.just("Product found"))
                .when(responseSpec).bodyToMono(String.class);

        // stub save
        OrderEntity orderEntity = OrderEntity.builder()
                .status(OrderStatus.PENDING)
                .build();
        when(orderRepository.save(any())).thenReturn(orderEntity);

        OrderResponseDTO result = orderService.createOrder(dto);

        assertNotNull(result);
        assertEquals(OrderStatus.PENDING, result.getStatus());
    }


    @Test
    @Order(2)
    void createOrder_userServiceFails_throwsException() {
        // Prepare the DTO for the order creation request
        OrderRequestDTO dto = new OrderRequestDTO(4L, 4L, 1);


        // Mock WebClient calls for user service (simulate failure)        doReturn(requestHeadersUriSpec).when(webClient).get();
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec)
                .uri("http://localhost:8081/users/{id}", dto.getUserId());
        doReturn(responseSpec).when(requestHeadersSpec).retrieve();
        doReturn(Mono.error(new RuntimeException("Service unavailable")))
                .when(responseSpec).bodyToMono(String.class);

        // Call the service method to create an order and expect it to throw ExternalServiceException
        assertThrows(ExternalServiceException.class, () -> orderService.createOrder(dto));
    }
}
