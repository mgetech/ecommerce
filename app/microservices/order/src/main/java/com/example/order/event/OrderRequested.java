package com.example.order.event;

public record OrderRequested(Long orderId, Long userId, Long productId, int quantity) {}

