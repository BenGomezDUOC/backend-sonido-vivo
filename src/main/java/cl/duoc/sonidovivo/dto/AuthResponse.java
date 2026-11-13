package cl.duoc.sonidovivo.dto;

public class AuthResponse {

    private String token;
    private String tipo = "Bearer";
    private UsuarioDto usuario;

    public AuthResponse() {
    }

    public AuthResponse(String token, UsuarioDto usuario) {
        this.token = token;
        this.tipo = "Bearer";
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public UsuarioDto getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDto usuario) {
        this.usuario = usuario;
    }
}
