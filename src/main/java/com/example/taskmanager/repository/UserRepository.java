package com.example.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskmanager.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email); //Verifica si un usuario con el email dado ya existe, valor único. Consulta la base de datos

}
