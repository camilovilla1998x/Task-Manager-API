package com.example.taskmanager.service;

import java.util.List;

import com.example.taskmanager.dto.task.TaskRequest;
import com.example.taskmanager.dto.task.TaskResponse;

public interface TaskService {

    TaskResponse create(TaskRequest request);

    List<TaskResponse> getAll();

    TaskResponse getById(Long id);

    TaskResponse update(Long id, TaskRequest request);

    void deleteById(Long id);

}
