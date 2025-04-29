package com.example.User.service;

import com.example.User.dto.*;

public interface UserService {
    UserResponseDTO create(UserRequestDTO request);
    UserResponseDTO getById(Long id);
    UserResponseDTO update(Long id, UserRequestDTO request);
    void delete(Long id);
}
