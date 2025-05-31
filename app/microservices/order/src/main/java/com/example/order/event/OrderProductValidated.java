package com.example.order.event;

public record OrderProductValidated(String orderId, boolean ok, String reason) {}

