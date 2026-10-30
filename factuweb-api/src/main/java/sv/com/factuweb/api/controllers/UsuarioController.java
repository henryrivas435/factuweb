package sv.com.factuweb.api.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sv.com.factuweb.api.dto.MensajeDTO;
import sv.com.factuweb.api.dto.UsuarioDTO;
import sv.com.factuweb.api.security.UsuarioPrincipal;
import sv.com.factuweb.api.interfaces.IUsuarioService;

import java.util.List;

/**
 * Gestion de vendedores. Todo el controlador esta restringido al
 * administrador del negocio.
 */
@RestController
@RequestMapping("/api/usuarios")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listar(@AuthenticationPrincipal UsuarioPrincipal u) {
        return ResponseEntity.ok(usuarioService.listar(u.getIdEmpresa()));
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> crear(@Valid @RequestBody UsuarioDTO peticion,
                                            @AuthenticationPrincipal UsuarioPrincipal u) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.crear(peticion, u.getIdEmpresa()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizar(@PathVariable Long id,
                                                 @Valid @RequestBody UsuarioDTO peticion,
                                                 @AuthenticationPrincipal UsuarioPrincipal u) {
        return ResponseEntity.ok(usuarioService.actualizar(id, peticion, u.getIdEmpresa()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeDTO> desactivar(@PathVariable Long id,
                                                 @AuthenticationPrincipal UsuarioPrincipal u) {
        usuarioService.desactivar(id, u.getIdEmpresa(), u.getIdUsuario());
        return ResponseEntity.ok(new MensajeDTO("Cuenta desactivada"));
    }

    @PatchMapping("/{id}/reactivar")
    public ResponseEntity<MensajeDTO> reactivar(@PathVariable Long id,
                                                @AuthenticationPrincipal UsuarioPrincipal u) {
        usuarioService.reactivar(id, u.getIdEmpresa());
        return ResponseEntity.ok(new MensajeDTO("Cuenta reactivada"));
    }
}
