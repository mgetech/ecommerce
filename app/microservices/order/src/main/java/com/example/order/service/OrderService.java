package com.example.order.service;

import com.example.order.dto.OrderRequestDTO;
import com.example.order.dto.OrderResponseDTO;
import com.example.order.entity.OrderStatus;
import reactor.core.publisher.Mono;

public interface OrderService {
    OrderResponseDTO createSimpleOrder(OrderRequestDTO request);

    OrderResponseDTO createOrder(OrderRequestDTO request);
    OrderResponseDTO getOrder(Long id);

    OrderResponseDTO updateStatus(Long id, OrderStatus newStatus);
    Mono<OrderResponseDTO> createOrderAsync(OrderRequestDTO request);
}
