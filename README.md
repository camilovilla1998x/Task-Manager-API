# 📝 Task Manager API

API REST desarrollada con **Spring Boot** para la gestión de usuarios y tareas.  
El proyecto está enfocado en **buenas prácticas backend**, **tests de integración**, **validaciones**, **documentación con Swagger** y una arquitectura limpia.

---

## 🚀 Tecnologías usadas

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Validation
- H2 (testing)
- MySQL (runtime)
- MapStruct
- Lombok
- Swagger / OpenAPI (springdoc)
- JUnit 5 + MockMvc
- Maven

---

## 🧱 Arquitectura

```
controller
service
service.impl
repository
entity
dto (request / response)
mapper
exception
```

---

## 📦 Funcionalidades

### 👤 Users
- Crear usuario
- Listar usuarios
- Obtener usuario por ID
- Eliminar usuario
- Validaciones (email único, campos obligatorios)

### ✅ Tasks
- Crear tarea asociada a un usuario
- Listar tareas
- Obtener tarea por ID
- Eliminar tarea
- Estados: `TODO`, `IN_PROGRESS`, `DONE`

---

## 🧪 Tests de integración

Valida flujo completo:
Controller → Service → Repository → BD H2 → HTTP Response

---

## 📄 Swagger

```
http://localhost:8080/swagger-ui.html
```

---

## ▶️ Ejecutar

```bash
mvn spring-boot:run
```

## 🧪 Tests
```bash
mvn test
```

---

Hecho con ❤️ y paciencia 😄
