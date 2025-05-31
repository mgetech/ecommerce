package com.example.Product.event;

public record OrderRequested(String orderId, String userId, String productId, int quantity) {}

