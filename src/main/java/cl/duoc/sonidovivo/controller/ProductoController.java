package cl.duoc.sonidovivo.controller;

import cl.duoc.sonidovivo.model.Producto;
import cl.duoc.sonidovivo.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para el catálogo de Tienda Sonido Vivo.
 * Expone endpoints HTTP consumibles por clientes REST, Swagger UI y el frontend en React.
 */
@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:5173") // Habilita CORS para el frontend React en Vite
@Tag(name = "Catálogo de Productos", description = "Operaciones CRUD para audio profesional en Tienda Sonido Vivo")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar productos", description = "Retorna la colección completa de productos disponibles o filtra por nombre opcional.")
    public List<Producto> listar(@RequestParam(required = false) String buscar) {
        if (buscar != null && !buscar.isBlank()) {
            return service.buscarPorNombre(buscar.trim());
        }
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar producto por ID", description = "Retorna un producto específico según su identificador numérico.")
    public Producto buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Filtrar por categoría", description = "Retorna todos los productos pertenecientes a una categoría específica.")
    public List<Producto> listarPorCategoria(@PathVariable String categoria) {
        return service.buscarPorCategoria(categoria);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear nuevo producto", description = "Registra un nuevo equipo de audio en la base de datos previa validación de campos.")
    public Producto crear(@Valid @RequestBody Producto producto) {
        return service.crear(producto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto existente", description = "Modifica los atributos de un producto registrado según su ID.")
    public Producto actualizar(@PathVariable Long id, @Valid @RequestBody Producto cambios) {
        return service.actualizar(id, cambios);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar producto", description = "Remueve un producto de la base de datos permanentemente.")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
