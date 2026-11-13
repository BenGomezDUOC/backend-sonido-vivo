package cl.duoc.sonidovivo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Entidad JPA que representa un producto de audio profesional en Tienda Sonido Vivo.
 */
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Column(nullable = false, length = 150)
    private String nombre;

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 1, message = "El precio debe ser mayor a 0 CLP")
    @Column(nullable = false)
    private Integer precio;

    @NotBlank(message = "La categoría es obligatoria")
    @Column(nullable = false, length = 80)
    private String categoria;

    @Column(length = 255)
    private String imagen;

    @Column(length = 500)
    private String descripcion;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    public Producto() {
    }

    public Producto(Long id, String nombre, Integer precio, String categoria, String imagen, String descripcion, Integer stock) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.stock = stock;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getImagenUrl() {
        return imagen;
    }

    public void setImagenUrl(String imagenUrl) {
        if (imagenUrl != null && !imagenUrl.isBlank()) {
            this.imagen = imagenUrl;
        }
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
