package com.example.user.service;

import com.example.user.dto.*;

public interface UserService {
    UserResponseDTO create(UserRequestDTO request);
    UserResponseDTO getById(Long id);
    UserResponseDTO update(Long id, UserRequestDTO request);
    void delete(Long id);
}
