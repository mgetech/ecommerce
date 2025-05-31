package com.example.order.event;

public record OrderUserValidated(String orderId, boolean ok, String reason) {}

