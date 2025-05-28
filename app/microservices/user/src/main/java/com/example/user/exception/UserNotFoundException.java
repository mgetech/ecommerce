package com.example.user.exception;


public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("user not found with ID " + id);
    }
}
