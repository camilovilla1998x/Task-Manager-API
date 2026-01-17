package com.example.taskmanager.exception;

public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException(Long id) {
        super("User Not Found with id: " + id);
    }

}
