package com.example.Product.event;

public record OrderProductValidated(String orderId, boolean ok, String reason) {}

