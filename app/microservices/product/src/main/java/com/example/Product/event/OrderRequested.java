package com.example.Product.event;

public record OrderRequested(Long orderId, Long userId, Long productId, int quantity) {}

