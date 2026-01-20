package com.example.taskmanager.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class ValidationErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private List<FieldErrorResponse> errors;

}

//* Errores de validación */