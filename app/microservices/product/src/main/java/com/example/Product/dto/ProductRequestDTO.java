package com.example.Product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ProductRequestDTO(
        @NotBlank(message = "Product name is required")
        String name,

        String description,

        @Positive(message = "Price must be greater than 0")
        Double price
) {}

