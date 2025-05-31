package com.example.user.event;

public record OrderRequested(String orderId, String userId, String productId, int quantity) {}

