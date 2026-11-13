package cl.duoc.sonidovivo.dto;

import cl.duoc.sonidovivo.model.Rol;
import cl.duoc.sonidovivo.model.Usuario;

public class UsuarioDto {

    private Long id;
    private String nombre;
    private String email;
    private Rol rol;
    private Boolean esDuoc;

    public UsuarioDto() {
    }

    public UsuarioDto(Long id, String nombre, String email, Rol rol, Boolean esDuoc) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.esDuoc = esDuoc;
    }

    public static UsuarioDto fromEntity(Usuario u) {
        if (u == null) return null;
        return new UsuarioDto(u.getId(), u.getNombre(), u.getEmail(), u.getRol(), u.getEsDuoc());
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Boolean getEsDuoc() {
        return esDuoc;
    }

    public void setEsDuoc(Boolean esDuoc) {
        this.esDuoc = esDuoc;
    }
}
