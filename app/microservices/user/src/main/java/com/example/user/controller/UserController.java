package com.example.user.controller;

import com.example.user.service.UserService;
import com.example.user.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "user API", description = "Operations related to user accounts")
public class UserController {

    private final UserService userService;


    @Operation(summary = "Create a new user", description = "Creates a new user and returns the created user details")
    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid UserRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @Operation(summary = "Get user by ID", description = "Fetch a user by their unique ID - it already has 3 users in the database.")
    @GetMapping("/{id}")
    public UserResponseDTO get(@PathVariable Long id) {
        return userService.getById(id);
    }

    @Operation(summary = "Update user", description = "Updates the user data for the given ID")
    @PutMapping("/{id}")
    public UserResponseDTO update(@PathVariable Long id, @RequestBody @Valid UserRequestDTO request) {
        return userService.update(id, request);
    }

    @Operation(summary = "Delete user", description = "Deletes a user by their ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

