package com.example.Product.dto;

public record ProductResponseDTO(
        Long id,
        String name,
        String description,
        Double price
) {}

