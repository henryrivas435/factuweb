package sv.com.factuweb.api.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sv.com.factuweb.api.dto.ClienteDTO;
import sv.com.factuweb.api.dto.MensajeDTO;
import sv.com.factuweb.api.security.UsuarioPrincipal;
import sv.com.factuweb.api.interfaces.IClienteService;

import java.util.List;

/** CRUD de clientes. Es uno de los dos CRUD de tabla independiente. */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final IClienteService clienteService;

    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listar(@AuthenticationPrincipal UsuarioPrincipal u) {
        return ResponseEntity.ok(clienteService.listar(u.getIdEmpresa()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> obtener(@PathVariable Long id,
                                              @AuthenticationPrincipal UsuarioPrincipal u) {
        return ResponseEntity.ok(clienteService.obtener(id, u.getIdEmpresa()));
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> crear(@Valid @RequestBody ClienteDTO peticion,
                                            @AuthenticationPrincipal UsuarioPrincipal u) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clienteService.crear(peticion, u.getIdEmpresa()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> actualizar(@PathVariable Long id,
                                                 @Valid @RequestBody ClienteDTO peticion,
                                                 @AuthenticationPrincipal UsuarioPrincipal u) {
        return ResponseEntity.ok(clienteService.actualizar(id, peticion, u.getIdEmpresa()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<MensajeDTO> eliminar(@PathVariable Long id,
                                               @AuthenticationPrincipal UsuarioPrincipal u) {
        clienteService.eliminar(id, u.getIdEmpresa());
        return ResponseEntity.ok(new MensajeDTO("Cliente eliminado"));
    }
}
