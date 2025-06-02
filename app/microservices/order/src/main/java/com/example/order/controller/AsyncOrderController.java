package com.example.order.controller;

import com.example.order.dto.OrderRequestDTO;
import com.example.order.dto.OrderResponseDTO;
import com.example.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Profile({"local", "docker"})
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order API - Async", description = "Handles async order creation using **Kafka**")
public class AsyncOrderController {

    private final OrderService orderService;

    @Operation(
            summary = "Create a new order Asynchronously",
            description = "**Only on Docker/Locally profiles** — Creates an order by publishing an event to Kafka. The order is confirmed only after user and product validation events are received asynchronously from user and product services."
    )
    @PostMapping("/async")
    public Mono<ResponseEntity<OrderResponseDTO>> createAsync(@RequestBody @Valid OrderRequestDTO request) {
        return orderService.createOrderAsync(request)
                .map(resp -> ResponseEntity.status(HttpStatus.CREATED).body(resp));
    }
}

