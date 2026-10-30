package sv.com.factuweb.api.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sv.com.factuweb.api.dto.auth.LoginRequestDTO;
import sv.com.factuweb.api.dto.auth.LoginResponseDTO;
import sv.com.factuweb.api.dto.auth.RegistroNegocioDTO;
import sv.com.factuweb.api.security.UsuarioPrincipal;
import sv.com.factuweb.api.interfaces.IAuthService;

import java.util.LinkedHashMap;
import java.util.Map;

/** Rutas abiertas: inicio de sesion y registro de negocio. */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO peticion) {
        return ResponseEntity.ok(authService.iniciarSesion(peticion));
    }

    @PostMapping("/registro")
    public ResponseEntity<LoginResponseDTO> registrar(
            @Valid @RequestBody RegistroNegocioDTO peticion) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.registrarNegocio(peticion));
    }

    /**
     * Datos de la sesion actual. Sirve al frontend para saber quien esta
     * conectado.
     *
     * Toda la rama /api/auth esta abierta, asi que aqui puede entrar una
     * peticion sin token: en ese caso el principal llega en null y hay que
     * responder 401 a mano. Sin esta comprobacion se caia con un
     * NullPointerException y el usuario veia un error 500.
     */
    @GetMapping("/yo")
    public ResponseEntity<Map<String, Object>> sesionActual(
            @AuthenticationPrincipal UsuarioPrincipal usuario) {
        if (usuario == null) {
            Map<String, Object> error = new LinkedHashMap<>();
            error.put("mensaje", "Debe iniciar sesión para acceder a este recurso");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        Map<String, Object> datos = new LinkedHashMap<>();
        datos.put("idUsuario", usuario.getIdUsuario());
        datos.put("nombre", usuario.getNombre());
        datos.put("email", usuario.getEmail());
        datos.put("rol", usuario.getRol().name());
        datos.put("idEmpresa", usuario.getIdEmpresa());
        return ResponseEntity.ok(datos);
    }
}
