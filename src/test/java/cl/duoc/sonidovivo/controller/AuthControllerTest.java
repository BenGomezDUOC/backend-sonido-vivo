package cl.duoc.sonidovivo.controller;

import cl.duoc.sonidovivo.dto.LoginRequest;
import cl.duoc.sonidovivo.dto.RegistroRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("1. POST /api/auth/login con credenciales válidas de ADMIN retorna token JWT")
    void testLoginAdminExitoso() throws Exception {
        LoginRequest req = new LoginRequest("admin@sonidovivo.cl", "duoc123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.usuario.email", is("admin@sonidovivo.cl")))
                .andExpect(jsonPath("$.usuario.rol", is("ROLE_ADMIN")));
    }

    @Test
    @DisplayName("2. POST /api/auth/login con credenciales válidas de CLIENTE retorna token JWT y flag esDuoc")
    void testLoginClienteExitoso() throws Exception {
        LoginRequest req = new LoginRequest("alumno@duocuc.cl", "duoc123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.usuario.rol", is("ROLE_CLIENTE")))
                .andExpect(jsonPath("$.usuario.esDuoc", is(true)));
    }

    @Test
    @DisplayName("3. POST /api/auth/login con credenciales incorrectas retorna HTTP 401")
    void testLoginPasswordIncorrecto() throws Exception {
        LoginRequest req = new LoginRequest("admin@sonidovivo.cl", "passwordEquivocada");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("4. POST /api/auth/registro crea un usuario válido y retorna HTTP 201 con JWT")
    void testRegistroExitoso() throws Exception {
        RegistroRequest req = new RegistroRequest(
                "Carlos Valenzuela",
                "carlos.valenzuela.test@gmail.com",
                "claveSegura123",
                LocalDate.of(1995, 5, 20)
        );

        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.usuario.nombre", is("Carlos Valenzuela")))
                .andExpect(jsonPath("$.usuario.email", is("carlos.valenzuela.test@gmail.com")))
                .andExpect(jsonPath("$.usuario.rol", is("ROLE_CLIENTE")))
                .andExpect(jsonPath("$.usuario.esDuoc", is(false)));
    }

    @Test
    @DisplayName("5. POST /api/auth/registro con menor de 18 años es rechazado con HTTP 400")
    void testRegistroMenorDeEdad() throws Exception {
        RegistroRequest req = new RegistroRequest(
                "Menor de Edad",
                "menor@test.com",
                "claveSegura123",
                LocalDate.now().minusYears(15) // 15 años
        );

        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("6. POST /api/auth/registro con email Duoc activa automáticamente esDuoc = true")
    void testRegistroEmailDuoc() throws Exception {
        RegistroRequest req = new RegistroRequest(
                "Estudiante Duoc",
                "estudiante.nuevo@duocuc.cl",
                "claveSegura123",
                LocalDate.of(2000, 1, 15)
        );

        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.usuario.esDuoc", is(true)));
    }
}
