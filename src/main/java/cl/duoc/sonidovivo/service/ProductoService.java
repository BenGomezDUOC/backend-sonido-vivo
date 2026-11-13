package cl.duoc.sonidovivo.service;

import cl.duoc.sonidovivo.model.Producto;
import cl.duoc.sonidovivo.repository.ProductoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Capa de Servicios: Centraliza la lógica de negocio y las operaciones sobre los productos.
 */
@Service
public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    /**
     * Retorna la lista completa de productos.
     */
    public List<Producto> listar() {
        return repository.findAll();
    }

    /**
     * Busca un producto por su ID único. Si no existe, arroja HTTP 404.
     */
    public Producto buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Producto con ID " + id + " no encontrado en el catálogo de Sonido Vivo."
                ));
    }

    /**
     * Registra un nuevo producto en el catálogo.
     * Garantiza que el cliente no imponga un ID arbitrario.
     */
    public Producto crear(Producto producto) {
        producto.setId(null);
        if (producto.getStock() == null) {
            producto.setStock(10); // Stock inicial por defecto
        }
        return repository.save(producto);
    }

    /**
     * Actualiza los datos de un producto existente.
     */
    public Producto actualizar(Long id, Producto cambios) {
        Producto actual = buscar(id);
        actual.setNombre(cambios.getNombre());
        actual.setPrecio(cambios.getPrecio());
        actual.setCategoria(cambios.getCategoria());
        if (cambios.getImagen() != null && !cambios.getImagen().isBlank()) {
            actual.setImagen(cambios.getImagen());
        }
        if (cambios.getDescripcion() != null) {
            actual.setDescripcion(cambios.getDescripcion());
        }
        if (cambios.getStock() != null) {
            actual.setStock(cambios.getStock());
        }
        return repository.save(actual);
    }

    /**
     * Elimina un producto por su ID.
     */
    public void eliminar(Long id) {
        Producto actual = buscar(id);
        repository.delete(actual);
    }

    /**
     * Filtra productos por categoría.
     */
    public List<Producto> buscarPorCategoria(String categoria) {
        return repository.findByCategoriaIgnoreCase(categoria);
    }

    /**
     * Filtra productos por texto de búsqueda.
     */
    public List<Producto> buscarPorNombre(String texto) {
        return repository.findByNombreContainingIgnoreCase(texto);
    }
}
