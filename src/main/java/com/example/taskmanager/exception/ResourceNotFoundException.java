package com.example.taskmanager.exception;

public abstract class ResourceNotFoundException extends RuntimeException { // abstract no se instancia directamente y sirve como base para otras excepciones
    public ResourceNotFoundException(String message) {
        super(message);
    }

}
