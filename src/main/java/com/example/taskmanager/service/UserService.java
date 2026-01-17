package com.example.taskmanager.service;

import java.util.List;

import com.example.taskmanager.dto.user.UserRequest;
import com.example.taskmanager.dto.user.UserResponse;

public interface UserService {

    UserResponse create(UserRequest request);

    List<UserResponse> getAll();
    
    UserResponse getById(Long id);

    void deleteById(Long id);
    
}
