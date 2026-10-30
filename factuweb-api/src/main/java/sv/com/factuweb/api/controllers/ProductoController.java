package sv.com.factuweb.api.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sv.com.factuweb.api.dto.MensajeDTO;
import sv.com.factuweb.api.dto.ProductoDTO;
import sv.com.factuweb.api.security.UsuarioPrincipal;
import sv.com.factuweb.api.interfaces.IProductoService;

import java.util.List;

/**
 * CRUD del stock. Es el otro CRUD de tabla independiente.
 *
 * Los vendedores pueden consultar, pero solo el administrador puede crear,
 * modificar o dar de baja productos.
 */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final IProductoService productoService;

    public ProductoController(IProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> list(
            @RequestParam(defaultValue = "false") boolean incluirInactivos,
            @AuthenticationPrincipal UsuarioPrincipal u) {
        return ResponseEntity.ok(productoService.list(u.getIdEmpresa(), incluirInactivos));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoDTO>> find(@RequestParam String texto,
                                                    @AuthenticationPrincipal UsuarioPrincipal u) {
        return ResponseEntity.ok(productoService.find(u.getIdEmpresa(), texto));
    }

    @GetMapping("/bajo-minimo")
    public ResponseEntity<List<ProductoDTO>> findBelowMinimum(
            @AuthenticationPrincipal UsuarioPrincipal u) {
        return ResponseEntity.ok(productoService.findBelowMinimum(u.getIdEmpresa()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> get(@PathVariable Long id,
                                               @AuthenticationPrincipal UsuarioPrincipal u) {
        return ResponseEntity.ok(productoService.get(id, u.getIdEmpresa()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ProductoDTO> create(@Valid @RequestBody ProductoDTO peticion,
                                             @AuthenticationPrincipal UsuarioPrincipal u) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productoService.create(peticion, u.getIdEmpresa()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ProductoDTO> update(@PathVariable Long id,
                                                  @Valid @RequestBody ProductoDTO peticion,
                                                  @AuthenticationPrincipal UsuarioPrincipal u) {
        return ResponseEntity.ok(productoService.update(id, peticion, u.getIdEmpresa()));
    }

    /** Baja logica: el producto se marca inactivo, no se elimina. */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<MensajeDTO> deactivate(@PathVariable Long id,
                                                 @AuthenticationPrincipal UsuarioPrincipal u) {
        productoService.deactivate(id, u.getIdEmpresa());
        return ResponseEntity.ok(new MensajeDTO(
                "Producto marcado como inactivo. Las facturas emitidas conservan su informacion."));
    }

    @PatchMapping("/{id}/reactivar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ProductoDTO> reactivate(@PathVariable Long id,
                                                 @AuthenticationPrincipal UsuarioPrincipal u) {
        return ResponseEntity.ok(productoService.reactivate(id, u.getIdEmpresa()));
    }
}
