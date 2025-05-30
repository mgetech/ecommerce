package com.example.user.event;

public record OrderUserValidated(Long orderId, boolean ok, String reason) {}

