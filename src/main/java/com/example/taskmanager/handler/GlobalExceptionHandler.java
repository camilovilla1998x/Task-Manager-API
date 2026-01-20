package com.example.taskmanager.handler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.taskmanager.dto.ApiErrorResponse;
import com.example.taskmanager.dto.FieldErrorResponse;
import com.example.taskmanager.dto.ValidationErrorResponse;
import com.example.taskmanager.exception.EmailAlreadyExistsException;
import com.example.taskmanager.exception.ResourceNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleEmailConflict(EmailAlreadyExistsException ex) {
        
        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Email Conflict",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex) {

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Resource Not Found",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationErrors( // Errores de múltiples inputs
            MethodArgumentNotValidException ex) {
    
        List<FieldErrorResponse> errores = ex.getBindingResult() // Obtiene la lista de errores de validación
                                        .getFieldErrors()       // Convierte cada error en un FieldErrorResponse
                                        .stream()               // Procesa la lista como un stream
                                        .map(error ->           // Mapea cada error
                                            new FieldErrorResponse(
                                                error.getField(),   // Nombre del campo con error
                                                error.getDefaultMessage()))  // Mensaje de error asociado
                                        .toList();

        ValidationErrorResponse response = new ValidationErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                errores
        );

        return ResponseEntity.badRequest().body(response);
    }
    
}
