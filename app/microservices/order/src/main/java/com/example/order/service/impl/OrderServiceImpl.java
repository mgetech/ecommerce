package com.example.order.service.impl;

import com.example.order.dto.OrderRequestDTO;
import com.example.order.dto.OrderResponseDTO;
import com.example.order.entity.OrderEntity;
import com.example.order.entity.OrderStatus;
import com.example.order.exception.ExternalServiceException;
import com.example.order.exception.OrderNotFoundException;
import com.example.order.repository.OrderRepository;
import com.example.order.service.OrderService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.core.env.Environment;


import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final Environment env;
    private String userBase;
    private String productBase;

    @PostConstruct
    private void init() {
        this.userBase = env.getProperty("user.service.url");
        this.productBase = env.getProperty("product.service.url");
    }

    @Override
    @Transactional
    public OrderResponseDTO createSimpleOrder(OrderRequestDTO request) {
        log.info("Simple create for user {} product {}",
                request.getUserId(), request.getProductId());

        OrderEntity order = OrderEntity.builder()
                .userId(request.getUserId())
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        order = orderRepository.save(order);
        return toResponse(order);
    }

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO request) {
        log.info("Creating order for user {} and product {}", request.getUserId(), request.getProductId());

        WebClient webClient = webClientBuilder.build();

        // 1. Validate user
        try {
            webClient.get()
                    .uri(userBase + "/users/{id}", request.getUserId())
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(), response -> {
                        log.info("User not found: {}", response.statusCode());
                        return Mono.error(new ExternalServiceException("User not found"));
                    })
                    .onStatus(status -> status.is5xxServerError(), response -> {
                        log.info("User service error: {}", response.statusCode());
                        return Mono.error(new ExternalServiceException("User service unavailable"));
                    })
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.info("User service call failed: {}", e.getMessage());
            throw new ExternalServiceException("User service unavailable");
        }

        // 2. Validate product
        try {
            webClient.get()
                    .uri(productBase + "/products/{id}", request.getProductId())
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(), response -> {
                        log.info("Product not found: {}", response.statusCode());
                        return Mono.error(new ExternalServiceException("Product not found"));
                    })
                    .onStatus(status -> status.is5xxServerError(), response -> {
                        log.info("Product service error: {}", response.statusCode());
                        return Mono.error(new ExternalServiceException("Product service unavailable"));
                    })
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.info("Product service call failed: {}", e.getMessage());
            throw new ExternalServiceException("Product service unavailable");
        }

        // 3. Save order
        OrderEntity orderEntity = OrderEntity.builder()
                .userId(request.getUserId())
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        orderRepository.save(orderEntity);

        return toResponse(orderEntity);
    }

    @Override
    public OrderResponseDTO getOrder(Long id) {
        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID " + id));
        return toResponse(orderEntity);
    }

    @Override
    public OrderResponseDTO updateStatus(Long id, OrderStatus newStatus) {
        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id " + id));
        orderEntity.setStatus(newStatus);
        orderRepository.save(orderEntity);
        return toResponse(orderEntity);
    }

    private OrderResponseDTO toResponse(OrderEntity orderEntity) {
        return OrderResponseDTO.builder()
                .id(orderEntity.getId())
                .userId(orderEntity.getUserId())
                .productId(orderEntity.getProductId())
                .quantity(orderEntity.getQuantity())
                .status(orderEntity.getStatus())
                .createdAt(orderEntity.getCreatedAt())
                .build();
    }
}
