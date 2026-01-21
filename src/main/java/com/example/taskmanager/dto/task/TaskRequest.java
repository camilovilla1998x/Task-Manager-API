package com.example.taskmanager.dto.task;

import com.example.taskmanager.entity.TaskStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Schema(description = "Request DTO for creating or updating a task")
public class TaskRequest {

    @Schema(description = "Title of the task", example = "Complete project documentation")
    @NotBlank(message = "Title must not be blank")
    @Size(max = 150, message = "Title must not exceed 150 characters")
    private String title;
    
    @Schema(description = "Description of the task", example = "Write detailed documentation for the project")
    private String description;

    @Schema(description = "Status of the task", example = "IN_PROGRESS")
    private TaskStatus status;

    @Schema(description = "ID of the user to whom the task is assigned", example = "1")
    @NotNull(message = "User id must not be null")
    private Long userId;

}
