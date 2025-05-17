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

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order API", description = "Handles order creation, retrieval, and status updates")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Create a new order", description = "Creates an order by getting userID and productID validation from User and Product services.")
    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(@RequestBody @Valid OrderRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
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
