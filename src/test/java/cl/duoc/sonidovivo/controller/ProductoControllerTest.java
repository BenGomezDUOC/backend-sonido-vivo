package cl.duoc.sonidovivo.controller;

import cl.duoc.sonidovivo.model.Producto;
import cl.duoc.sonidovivo.repository.ProductoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductoRepository repository;

    @Test
    @DisplayName("1. GET /api/productos es público y retorna la lista de productos")
    void testListarProductos() throws Exception {
        mockMvc.perform(get("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(12))))
                .andExpect(jsonPath("$[0].nombre", notNullValue()));
    }

    @Test
    @DisplayName("2. GET /api/productos/{id} retorna el producto por ID")
    void testBuscarPorIdExistente() throws Exception {
        Long primerId = repository.findAll().getFirst().getId();

        mockMvc.perform(get("/api/productos/" + primerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(primerId.intValue())));
    }

    @Test
    @DisplayName("3. POST /api/productos sin autenticación es rechazado con HTTP 401 o 403")
    void testCrearProductoSinAuthRechazado() throws Exception {
        Producto nuevo = new Producto(null, "Monitores Sin Auth", 100000, "Monitores y Parlantes", null, null, 1);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("4. POST /api/productos con rol ADMIN crea el producto exitosamente")
    void testCrearProductoConRolAdmin() throws Exception {
        Producto nuevo = new Producto(null, "Monitores Neumann KH 120 II", 799990, "Monitores y Parlantes",
                "/img/neumann.jpg", "Monitores activos de estudio.", 4);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is("Monitores Neumann KH 120 II")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("5. POST /api/productos con precio <= 0 retorna HTTP 400 Bad Request")
    void testCrearProductoInvalido() throws Exception {
        Producto invalido = new Producto(null, "Cable Fallido", 0, "Accesorios", null, null, 1);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("6. PUT /api/productos/{id} con rol ADMIN actualiza los campos")
    void testActualizarProducto() throws Exception {
        Long primerId = repository.findAll().getFirst().getId();
        Producto cambios = new Producto(null, "Shure SM7B - Edición 50 Aniversario", 459990, "Micrófonos",
                "/img/sm7b.webp", "Edición conmemorativa.", 10);

        mockMvc.perform(put("/api/productos/" + primerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cambios)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Shure SM7B - Edición 50 Aniversario")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("7. DELETE /api/productos/{id} con rol ADMIN elimina el producto")
    void testEliminarProducto() throws Exception {
        Producto temp = repository.save(new Producto(null, "Equipo Temporal Borrar", 50000, "Accesorios", null, null, 1));
        Long tempId = temp.getId();

        mockMvc.perform(delete("/api/productos/" + tempId))
                .andExpect(status().isNoContent());
    }
}
