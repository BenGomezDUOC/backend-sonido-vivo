package cl.duoc.sonidovivo.service;

import cl.duoc.sonidovivo.dto.*;
import cl.duoc.sonidovivo.model.Rol;
import cl.duoc.sonidovivo.model.Usuario;
import cl.duoc.sonidovivo.repository.UsuarioRepository;
import cl.duoc.sonidovivo.security.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Period;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Autentica credenciales y emite un token JWT.
     */
    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Correo electrónico o contraseña incorrectos."
                ));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Correo electrónico o contraseña incorrectos."
            );
        }

        String token = jwtUtils.generateToken(usuario.getEmail(), usuario.getRol().name());
        return new AuthResponse(token, UsuarioDto.fromEntity(usuario));
    }

    /**
     * Registra un nuevo cliente, valida mayoría de edad y regla de beneficio Duoc UC.
     */
    public AuthResponse registro(RegistroRequest request) {
        String emailNorm = request.getEmail().trim().toLowerCase();

        if (usuarioRepository.existsByEmail(emailNorm)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El correo electrónico ya se encuentra registrado."
            );
        }

        // Validación de mayoría de edad (>= 18 años)
        if (request.getFechaNacimiento() != null) {
            int edad = Period.between(request.getFechaNacimiento(), LocalDate.now()).getYears();
            if (edad < 18) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Debes ser mayor de 18 años para registrarte en la tienda."
                );
            }
        }

        // Regla institucional Duoc UC: Detección automática por dominio de correo
        boolean esDuoc = emailNorm.endsWith("@duocuc.cl") || emailNorm.endsWith("@profesor.duoc.cl");

        Usuario nuevo = new Usuario(
                null,
                request.getNombre().trim(),
                emailNorm,
                passwordEncoder.encode(request.getPassword()),
                Rol.ROLE_CLIENTE,
                request.getFechaNacimiento(),
                esDuoc
        );

        Usuario guardado = usuarioRepository.save(nuevo);
        String token = jwtUtils.generateToken(guardado.getEmail(), guardado.getRol().name());

        return new AuthResponse(token, UsuarioDto.fromEntity(guardado));
    }

    /**
     * Retorna los datos del usuario autenticado actual.
     */
    public UsuarioDto obtenerPerfil(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        return UsuarioDto.fromEntity(usuario);
    }
}
