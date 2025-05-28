package com.example.user.service.impl;

import com.example.user.dto.*;
import com.example.user.entity.User;
import com.example.user.exception.UserNotFoundException;
import com.example.user.repository.UserRepository;
import com.example.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(cacheNames = "users", key = "#id")
    public UserResponseDTO getById(Long id) {
        return userRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    @CachePut(cacheNames = "users", key = "#id")
    public UserResponseDTO update(Long id, UserRequestDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setName(request.name());
        user.setEmail(request.email());
        return toDto(userRepository.save(user));
    }

    @Override
    @CacheEvict(cacheNames = "users", key = "#id")
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
