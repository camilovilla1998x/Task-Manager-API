package com.example.taskmanager.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Schema(description = "Request DTO for creating a new user")
public class UserRequest {

    @Schema(description = "Name of the user", example = "Camilo")
    @NotBlank(message = "Name must not be blank")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Schema(description = "Email of the user", example = "camilo@test.com")
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email must not be blank")
    private String email;
}
