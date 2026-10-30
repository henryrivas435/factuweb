package sv.com.factuweb.api.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sv.com.factuweb.api.dto.EmpresaDTO;
import sv.com.factuweb.api.security.UsuarioPrincipal;
import sv.com.factuweb.api.interfaces.IEmpresaService;

/** Datos del propio negocio: los que aparecen en el encabezado de la factura. */
@RestController
@RequestMapping("/api/empresa")
public class EmpresaController {

    private final IEmpresaService empresaService;

    public EmpresaController(IEmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @GetMapping
    public ResponseEntity<EmpresaDTO> miNegocio(@AuthenticationPrincipal UsuarioPrincipal u) {
        return ResponseEntity.ok(empresaService.obtener(u.getIdEmpresa()));
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<EmpresaDTO> actualizar(@Valid @RequestBody EmpresaDTO datos,
                                                 @AuthenticationPrincipal UsuarioPrincipal u) {
        return ResponseEntity.ok(empresaService.actualizar(u.getIdEmpresa(), datos));
    }
}
