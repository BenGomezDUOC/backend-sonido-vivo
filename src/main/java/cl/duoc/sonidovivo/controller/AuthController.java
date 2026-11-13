package cl.duoc.sonidovivo.controller;

import cl.duoc.sonidovivo.dto.*;
import cl.duoc.sonidovivo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Controlador de Autenticación y Registro con JWT.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Autenticación & Sesiones", description = "Endpoints de login, registro de clientes y validación de tokens JWT")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Valida credenciales y genera un token JWT con el rol correspondiente.")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar nuevo cliente", description = "Crea una cuenta de cliente, calcula beneficio Duoc UC y retorna sesión con token JWT.")
    public AuthResponse registro(@Valid @RequestBody RegistroRequest request) {
        return authService.registro(request);
    }

    @GetMapping("/me")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Perfil de sesión activa", description = "Obtiene los datos del usuario autenticado a partir del token JWT.")
    public UsuarioDto miPerfil(Principal principal) {
        if (principal == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Sesión no autenticada."
            );
        }
        return authService.obtenerPerfil(principal.getName());
    }
}
