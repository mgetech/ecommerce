package com.example.order.event;

public record OrderProductValidated(Long orderId, boolean ok, String reason) {}

