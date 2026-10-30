package sv.com.factuweb.api.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sv.com.factuweb.api.dto.CategoriaDTO;
import sv.com.factuweb.api.dto.MensajeDTO;
import sv.com.factuweb.api.security.UsuarioPrincipal;
import sv.com.factuweb.api.interfaces.ICategoriaService;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final ICategoriaService categoriaService;

    public CategoriaController(ICategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> list(@AuthenticationPrincipal UsuarioPrincipal u) {
        return ResponseEntity.ok(categoriaService.list(u.getIdEmpresa()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> get(@PathVariable Long id,
                                                @AuthenticationPrincipal UsuarioPrincipal u) {
        return ResponseEntity.ok(categoriaService.get(id, u.getIdEmpresa()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<CategoriaDTO> create(@Valid @RequestBody CategoriaDTO peticion,
                                              @AuthenticationPrincipal UsuarioPrincipal u) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoriaService.create(peticion, u.getIdEmpresa()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<CategoriaDTO> update(@PathVariable Long id,
                                                   @Valid @RequestBody CategoriaDTO peticion,
                                                   @AuthenticationPrincipal UsuarioPrincipal u) {
        return ResponseEntity.ok(categoriaService.update(id, peticion, u.getIdEmpresa()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<MensajeDTO> delete(@PathVariable Long id,
                                               @AuthenticationPrincipal UsuarioPrincipal u) {
        categoriaService.delete(id, u.getIdEmpresa());
        return ResponseEntity.ok(new MensajeDTO("Categoria eliminada"));
    }
}
