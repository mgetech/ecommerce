package com.example.order.event;

public record OrderUserValidated(Long orderId, boolean ok, String reason) {}

