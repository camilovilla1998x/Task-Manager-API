package com.example.taskmanager.dto.task;

import java.time.LocalDateTime;

import com.example.taskmanager.entity.TaskStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private TaskStatus status;

    private Long userId;
    private String userName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
