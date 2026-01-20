package com.example.taskmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.taskmanager.dto.user.UserRequest;
import com.example.taskmanager.dto.user.UserResponse;
import com.example.taskmanager.entity.User;

@Mapper(componentModel = "spring") //
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    User toEntity(UserRequest request);

    UserResponse toResponse(User user);


    //MapStruct genera una clase automáticamente que implementa esta interfaz
    //Copia campos con el mismo nombre y tipo en tiempo de compilación
    //
}
