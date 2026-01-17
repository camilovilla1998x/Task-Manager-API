package com.example.taskmanager.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.taskmanager.dto.user.UserRequest;
import com.example.taskmanager.dto.user.UserResponse;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.exception.EmailAlreadyExistsException;
import com.example.taskmanager.exception.UserNotFoundException;
import com.example.taskmanager.mapper.UserMapper;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponse create(UserRequest request) {
        
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already in use: " + request.getEmail());
        }
        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Override
    public List<UserResponse> getAll() {
        return userRepository.findAll()
                            .stream()
                            .map(userMapper::toResponse)
                            .toList();
    }

    @Override
    public UserResponse getById(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toResponse(existingUser);
    }

    @Override
    public void deleteById(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(existingUser);
    }

}
