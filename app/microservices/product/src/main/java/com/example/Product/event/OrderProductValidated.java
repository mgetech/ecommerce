package com.example.Product.event;

public record OrderProductValidated(Long orderId, boolean ok, String reason) {}

