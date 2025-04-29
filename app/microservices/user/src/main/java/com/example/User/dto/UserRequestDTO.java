package com.example.User.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO(
        @NotBlank(message = "Name is required") String name,
        @Email(message = "Invalid email format") String email
) {}

