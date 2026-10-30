package sv.com.factuweb.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sv.com.factuweb.api.dto.EmpresaPublicaDTO;
import sv.com.factuweb.api.interfaces.IEmpresaService;

import java.util.List;

/**
 * Parte publica del sitio: no requiere iniciar sesion.
 * Solo expone la lista de negocios registrados, sin datos privados.
 */
@RestController
@RequestMapping("/api/publico")
public class PublicoController {

    private final IEmpresaService empresaService;

    public PublicoController(IEmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @GetMapping("/negocios")
    public ResponseEntity<List<EmpresaPublicaDTO>> negocios() {
        return ResponseEntity.ok(empresaService.listarPublicas());
    }
}
