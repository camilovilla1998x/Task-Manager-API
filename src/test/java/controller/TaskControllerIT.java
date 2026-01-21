package controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.taskmanager.TaskManagerApiApplication;
import com.example.taskmanager.dto.task.TaskRequest;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.TaskStatus;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(classes = TaskManagerApiApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TaskControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository; // Es necesario porque Tasks están asociadas a Users

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll(); // Limpiar tareas antes de cada prueba
        userRepository.deleteAll(); // Limpiar usuarios antes de cada prueba
    }

    //? Crear Task correctamente

    @Test
    void shouldCreateTaskSuccessfully() throws Exception {

        // Arrange -> Preparar datos y estado inicial (User real)
        User user = new User();
        user.setName("Camilo");
        user.setEmail("camilo@test.com");

        User savedUser = userRepository.save(user); // Guardar usuario en BD (H2 en memoria)

        //Arrange -> Preparar datos de TaskRequest 

        TaskRequest request = new TaskRequest();
        request.setTitle("Learn Spring Boot");
        request.setDescription("Integration testing");
        request.setStatus(TaskStatus.TODO);
        request.setUserId(savedUser.getId());

        // Act & Assert -> Realizar la acción y verificar el resultado
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.title").value("Learn Spring Boot"))
            .andExpect(jsonPath("$.status").value("TODO"))
            .andExpect(jsonPath("$.userId").value(savedUser.getId()))
            .andExpect(jsonPath("$.userName").value("Camilo"));


    }

    //? Crear Task inexistente User -> 404 Not Found
    /*
    Este test valida tres cosas críticas:

        La relación Task → User se valida correctamente

        El Service no permite FK inválidas

        El GlobalExceptionHandler responde como contrato
    
    */

    @Test
    void shouldReturn404WhenUserDoesNotExist() throws Exception {

        // Arrange -> Preparar datos de TaskRequest con userId inexistente
        TaskRequest request = new TaskRequest();
        request.setTitle("Task without user");
        request.setDescription("This should fail");
        request.setStatus(TaskStatus.TODO);
        request.setUserId(999L); // userId que no existe en BD

        // Act & Assert -> Realizar la acción y verificar el resultado
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message")
            .value("User Not Found with id: 999"));

    // Este test valida que:
    // 1. La petición HTTP entra por el controller -> Recibe el POST correctamente
    // 2. Pasa por el service -> Valida la existencia del User asociado
    // 3. Usa el respository real -> Consulta en la BD H2 en memoria
    // 4. Devuelve respuesta HTTP correcta + JSON correcto -> 404 Not Found + (Excepción)
    // 5. Handler mapea a 404 correctamente
    // 6. Response tiene estructura esperada (status, message)

    }

    //? Test -> Crear TASK  con title vacío -> 400 Bad Request
    /* 
    Este test valida que:
        Bean Validation se ejecuta (@NotBlank)

        Spring no entra al service

        El error se captura en el GlobalExceptionHandler

        La respuesta HTTP es 400

        El JSON de error es consistente


        -> Debe existir un User válido
        -> El title debe ser válido
        -> Si el User no existe -> 404
    */

    @Test
    void shouldReturn400WhenTitleIsBlank() throws Exception {

        //Arrange -> Preparar User válido
        User user = new User();
        user.setName("Camilo");
        user.setEmail("camilo@test.com");
        User savedUser = userRepository.save(user); // Guardar usuario en BD (H2 en memoria)

        //Arrange -> Preparar TaskRequest con title vacío
        TaskRequest request = new TaskRequest();
        request.setTitle(""); // Título vacío
        request.setDescription("Invalid Task");
        request.setStatus(TaskStatus.TODO);
        request.setUserId(savedUser.getId());

        // Act & Assert -> Realizar la acción y verificar el resultado
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest()) // 400 Bad Request
            .andExpect(jsonPath("$.status").value(400)) // Verificar campo status
            .andExpect(jsonPath("$.errors[0].field").value("title")) // Verifica que campo con error sea "title" de la lista
            .andExpect(jsonPath("$.errors[0].message").exists()); // Verificar que el mensaje de error exista
    }


    //? Test -> Obtener Task por ID correctamente

    @Test
    void shouldGetTaskByIdSuccessfully() throws Exception {

        // Arrange → User
    User user = new User();
    user.setName("Camilo");
    user.setEmail("camilo@test.com");
    User savedUser = userRepository.save(user);

    // Arrange → Task
    Task task = new Task();
    task.setTitle("Learn Spring Boot");
    task.setDescription("Integration testing");
    task.setStatus(TaskStatus.TODO);
    task.setUser(savedUser);

    Task savedTask = taskRepository.save(task);

    // Act & Assert
    mockMvc.perform(get("/api/tasks/{id}", savedTask.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(savedTask.getId()))
        .andExpect(jsonPath("$.title").value("Learn Spring Boot"))
        .andExpect(jsonPath("$.status").value("TODO"))
        .andExpect(jsonPath("$.userId").value(savedUser.getId()))
        .andExpect(jsonPath("$.userName").value("Camilo"))
        .andExpect(jsonPath("$.createdAt").exists())
        .andExpect(jsonPath("$.updatedAt").exists());


    }

    //? Test -> Obtener Task por ID inexistente -> 404 Not Found

    @Test
    void shouldReturn404WhenTaskNotFound() throws Exception {

        mockMvc.perform(get("/api/tasks/{id}", 999L))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message")
                .value("Task Not Found with id: 999"));
    }

    //? Test -> Obtener todas las Tasks correctamente
    @Test
    void shouldReturnAllTasks() throws Exception {

        // Arrange → User
        User user = new User();
        user.setName("Camilo");
        user.setEmail("camilo@test.com");
        User savedUser = userRepository.save(user);

        // Arrange → Task 1
        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setDescription("Desc 1");
        task1.setStatus(TaskStatus.TODO);
        task1.setUser(savedUser);
        taskRepository.save(task1);

        // Arrange → Task 2
        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Desc 2");
        task2.setStatus(TaskStatus.DONE);
        task2.setUser(savedUser);
        taskRepository.save(task2);

        // Act & Assert
        mockMvc.perform(get("/api/tasks"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").exists())
            .andExpect(jsonPath("$[0].title").exists())
            .andExpect(jsonPath("$[0].userId").value(savedUser.getId()))
            .andExpect(jsonPath("$[0].userName").value("Camilo"));
    }

    //? Test -> Obtener todas las Tasks cuando no hay ninguna -> Retorna lista vacía
    @Test
    void shouldReturnEmptyListWhenNoTasksExist() throws Exception {

        mockMvc.perform(get("/api/tasks"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
    }

    //? Test -> Eliminar Task por ID correctamente
    @Test
    void shouldDeleteTaskSuccessfully() throws Exception {

        // Arrange → User
        User user = new User();
        user.setName("Camilo");
        user.setEmail("camilo@test.com");
        User savedUser = userRepository.save(user);

        // Arrange → Task
        Task task = new Task();
        task.setTitle("Test task");
        task.setDescription("Task description");
        task.setStatus(TaskStatus.TODO);
        task.setUser(savedUser);
        Task savedTask = taskRepository.save(task);

        // Act
        mockMvc.perform(delete("/api/tasks/{id}", savedTask.getId()))
                .andExpect(status().isNoContent());

        // Assert
        assertFalse(taskRepository.existsById(savedTask.getId()));
    }

    //? Test -> Eliminar Task por ID inexistente -> 404 Not Found
    @Test
    void shouldReturn404WhenDeletingNonExistingTask() throws Exception {

        mockMvc.perform(delete("/api/tasks/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Task Not Found with id: 999"));
    }
}

//* Un test de integración en este contexto valida que
//* 1. La petición HTTP entra por el controller -> Resuelve la URL (/ api / tasks) -> Ejecuta el método del @RestController -> Aplica @RequestMapping, @PostMapping, etc.
//* 2. Pasa por el Service -> Llama TaskService -> Aplica lógica de negocio
//* 3. Usa el repository real -> Spring inyecta TaskRepository real (H2 en memoria) -> Guarda y recupera datos
//* 4. Persiste en BD real (H2 / Test DB) -> Los datos se insertan físicamente -> Se validan (NOT NULL, UNIQUE, FK, etc.) -> ¿Cómo? Usando @ActiveProfiles("test"), Spring carga application-test.yaml y se usa H2 en memoria
//* 5. Devuelve respuesta HTTP correcta + JSON correcto -> El status HTTP es correcto (201, 400, 404, 500, etc.) -> El cuerpo JSON tiene los campos esperados (id, name, createdAt, etc.)
//*
//*    */
