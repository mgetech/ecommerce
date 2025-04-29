package com.example.User.service.impl;

import com.example.User.dto.*;
import com.example.User.entity.User;
import com.example.User.exception.UserNotFoundException;
import com.example.User.repository.UserRepository;
import com.example.User.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDTO create(UserRequestDTO request) {
        User user = userRepository.save(User.builder()
                .name(request.name())
                .email(request.email())
                .build());
        return toDto(user);
    }

    @Override
    public UserResponseDTO getById(Long id) {
        return userRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public UserResponseDTO update(Long id, UserRequestDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setName(request.name());
        user.setEmail(request.email());
        return toDto(userRepository.save(user));
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    private UserResponseDTO toDto(User user) {
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
    }
}
