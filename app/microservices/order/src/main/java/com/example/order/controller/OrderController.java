package com.example.order.controller;

import com.example.order.dto.OrderRequestDTO;
import com.example.order.dto.OrderResponseDTO;
import com.example.order.entity.OrderStatus;
import com.example.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order API", description = "Handles order creation, retrieval, and status updates")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Create a new order", description = "Creates an order by getting userID and productID validation from user and Product services.")
    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(@RequestBody @Valid OrderRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }

    @Operation(summary = "Create a new order Asynchronously", description = "Creates an order by publishing an event to Kafka. The order is confirmed only after user and product validation events are received asynchronously from user and product services.")
    @PostMapping("/async")
    public Mono<ResponseEntity<OrderResponseDTO>> createAsync(@RequestBody @Valid OrderRequestDTO request) {
        return orderService.createOrderAsync(request)
                .map(resp -> ResponseEntity.status(HttpStatus.CREATED).body(resp));
    }

    @Operation(summary = "Get order by ID", description = "Fetches order details by order ID  - it already has 3 orders in the database.")
    @GetMapping("/{id}")
    public OrderResponseDTO get(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    @Operation(summary = "Update order status", description = "Changes the status of an existing order")
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status
    ) {
        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }

}
