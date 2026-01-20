package controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.taskmanager.TaskManagerApiApplication;
import com.example.taskmanager.dto.user.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(classes = TaskManagerApiApplication.class) //Levanta todo el contexto de Spring -> Usa repositorios reales (h2 en memoria)
@AutoConfigureMockMvc //Configura MockMvc para pruebas de controladores -> Inyecta MockMvc y permite simular peticiones HTTP
@ActiveProfiles("test") //Usa el perfil de configuración "test" -> Configuraciones específicas para pruebas (como base de datos en memoria)
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc; //Para simular peticiones HTTP a los controladores

    @Autowired
    private ObjectMapper objectMapper; //Para convertir objetos Java a JSON y viceversa
    
    //Test -> Crear usuario
    @Test
    void shouldCreateUserSuccessfully() throws Exception {

        //Arrange -> Preparar datos y estado inicial
        UserRequest request = new UserRequest();
        request.setName("Camilo");
        request.setEmail("camilo_" + UUID.randomUUID() + "@test.com"); //Email único para evitar conflictos en pruebas

        String jsonRequest = objectMapper.writeValueAsString(request); //Convertir objeto a JSON -> {"name":"Camilo","email":"camilo@test.com"}

        //Act & Assert -> Realizar la acción y verificar el resultado
        mockMvc.perform(post("/api/users")   //Simular petición POST a /api/users
                .contentType(MediaType.APPLICATION_JSON) //Tipo de contenido JSON
                .content(jsonRequest)) //Cuerpo de la petición -> JSON del usuario
            .andExpect(status().isCreated()) //Verificar que el estado HTTP sea 201 Created
            .andExpect(jsonPath("$.id").isNumber()) //Verificar que el campo id exista y sea un número
            .andExpect(jsonPath("$.name").value("Camilo")) //Verificar que el campo name sea "Camilo"
            .andExpect(jsonPath("$.email").value(request.getEmail())) //Verificar que el campo email sea el esperado "camilo_....@test.com"
            .andExpect(jsonPath("$.createdAt").exists()) //Verificar que el campo createdAt exista
            .andExpect(jsonPath("$.updatedAt").exists());//Verificar que el campo updatedAt exista

    }

    @Test
    void shouldReturn409WhenEmailAlreadyExists() throws Exception {

        //Arrange -> Preparar datos y estado inicial
        String email = "camilo_" + UUID.randomUUID() + "@test.com";

        String userJson = """
        {
        "name": "Camilo",
        "email": "%s"
        }
        """.formatted(email);
                
        //Primera creación -> OK
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
            .andExpect(status().isCreated());

        // 2️⃣ Act + Assert
        mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJson))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.status").value(409))
        .andExpect(jsonPath("$.message").value("Email already in use: " + email));
    }

    //* Inicio Validaciones 400 */
    


    



}
