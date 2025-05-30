package com.example.user.event;

public record OrderRequested(Long orderId, Long userId, Long productId, int quantity) {}

