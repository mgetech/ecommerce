package com.example.order.event;

public record OrderFinalized(String orderId, boolean confirmed, String reason) {}

