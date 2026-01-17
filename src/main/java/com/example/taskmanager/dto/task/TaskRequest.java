package com.example.taskmanager.dto.task;

import com.example.taskmanager.entity.TaskStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class TaskRequest {

    @NotBlank(message = "Title must not be blank")
    @Size(max = 150, message = "Title must not exceed 150 characters")
    private String title;
    
    @SuppressWarnings("unused")
    private String description;

    @NotNull(message = "Status must not be null")
    private TaskStatus status;

    @NotNull(message = "User id must not be null")
    private Long userId;

}
