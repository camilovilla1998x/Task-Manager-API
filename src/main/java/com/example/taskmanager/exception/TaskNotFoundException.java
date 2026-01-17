package com.example.taskmanager.exception;

public class TaskNotFoundException extends ResourceNotFoundException {

    public TaskNotFoundException(Long id) {
        super("Task Not Found with id: " + id);
    }

}
