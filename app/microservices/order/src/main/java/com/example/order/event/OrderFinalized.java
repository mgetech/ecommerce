package com.example.order.event;

public record OrderFinalized(Long orderId, boolean confirmed, String reason) {}

