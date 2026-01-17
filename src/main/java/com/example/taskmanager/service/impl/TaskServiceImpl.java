package com.example.taskmanager.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.taskmanager.dto.task.TaskRequest;
import com.example.taskmanager.dto.task.TaskResponse;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.exception.UserNotFoundException;
import com.example.taskmanager.mapper.TaskMapper;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.service.TaskService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    @Override
    public TaskResponse create(TaskRequest request) {
        
        //Busco el usuario
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(request.getUserId()));
        
        //Mappeo TaskRequest -> Task
        Task task = taskMapper.toEntity(request);

        //Asocio el usuario a la tarea (esto no lo hace MapStruct automaticamente)
        task.setUser(user);

        //Guardo la tarea
        Task savedTask = taskRepository.save(task);

        //devuelvo Response
        return taskMapper.toResponse(savedTask);
    }

    @Override
    public List<TaskResponse> getAll() {
        return taskRepository.findAll()
                            .stream()
                            .map(taskMapper::toResponse)
                            .toList();
    }

    @Override
    public TaskResponse getById(Long id) {

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        
        return taskMapper.toResponse(existingTask);
    }

    @Override
    public TaskResponse update(Long id, TaskRequest request) {
        Task existingTask = taskRepository.findById(id)
            .orElseThrow(() ->
                new TaskNotFoundException(id)
            );

    // Actualizar campos
    existingTask.setTitle(request.getTitle());
    existingTask.setDescription(request.getDescription());
    existingTask.setStatus(request.getStatus());

    // Si cambia el usuario
    if (!existingTask.getUser().getId().equals(request.getUserId())) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                    new UserNotFoundException(request.getUserId())
                );
        existingTask.setUser(user);
    }

    Task updated = taskRepository.save(existingTask);

    return taskMapper.toResponse(updated);
    }

    @Override
    public void deleteById(Long id) {

        Task existingTask = taskRepository.findById(id)
            .orElseThrow(() ->
                new TaskNotFoundException(id)
            );

        taskRepository.delete(existingTask);
       
    }

}
