package com.example.user.event;

public record OrderUserValidated(String orderId, boolean ok, String reason) {}

