package cl.duoc.sonidovivo.repository;

import cl.duoc.sonidovivo.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio Spring Data JPA para la entidad Producto.
 * Proporciona métodos CRUD automáticos (findAll, findById, save, delete, etc.)
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Busca productos filtrando por categoría (insensible a mayúsculas/minúsculas).
     */
    List<Producto> findByCategoriaIgnoreCase(String categoria);

    /**
     * Busca productos que contengan un texto en su nombre (para el buscador en tiempo real).
     */
    List<Producto> findByNombreContainingIgnoreCase(String texto);
}
