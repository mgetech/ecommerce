package com.example.order.event;

public record OrderRequested(String orderId, String userId, String productId, int quantity) {}

