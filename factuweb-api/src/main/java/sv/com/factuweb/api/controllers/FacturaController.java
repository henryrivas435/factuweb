package sv.com.factuweb.api.controllers;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sv.com.factuweb.api.dto.FacturaDTO;
import sv.com.factuweb.api.dto.FacturaResumenDTO;
import sv.com.factuweb.api.enums.EstadoFactura;
import sv.com.factuweb.api.security.UsuarioPrincipal;
import sv.com.factuweb.api.interfaces.IFacturaService;

import java.time.LocalDate;
import java.util.List;

/** Emision y consulta de facturas: la relacion maestro-detalle del sistema. */
@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    private final IFacturaService facturaService;

    public FacturaController(IFacturaService facturaService) {
        this.facturaService = facturaService;
    }

    /** Emite una factura. Aqui ocurre la transaccion principal del sistema. */
    @PostMapping
    public ResponseEntity<FacturaDTO> issue(@Valid @RequestBody FacturaDTO peticion,
                                             @AuthenticationPrincipal UsuarioPrincipal u) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(facturaService.issue(peticion, u.getIdEmpresa(), u.getIdUsuario()));
    }

    @GetMapping
    public ResponseEntity<List<FacturaResumenDTO>> list(
            @AuthenticationPrincipal UsuarioPrincipal u) {
        return ResponseEntity.ok(facturaService.list(u.getIdEmpresa()));
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<FacturaResumenDTO>> porPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) EstadoFactura estado,
            @AuthenticationPrincipal UsuarioPrincipal u) {
        return ResponseEntity.ok(
                facturaService.findByPeriod(u.getIdEmpresa(), desde, hasta, estado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaDTO> get(@PathVariable Long id,
                                              @AuthenticationPrincipal UsuarioPrincipal u) {
        return ResponseEntity.ok(facturaService.get(id, u.getIdEmpresa()));
    }

    /**
     * Verificacion publica de una factura: es la direccion a la que apunta
     * el codigo QR impreso en el documento. No requiere iniciar sesion.
     */
    @GetMapping("/verificar/{numero}")
    public ResponseEntity<FacturaDTO> verify(@PathVariable String numero) {
        return ResponseEntity.ok(facturaService.verify(numero));
    }

    /** Anula la factura y devuelve el stock. No elimina la fila. */
    @PatchMapping("/{id}/anular")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<FacturaDTO> cancel(@PathVariable Long id,
                                             @AuthenticationPrincipal UsuarioPrincipal u) {
        return ResponseEntity.ok(facturaService.cancel(id, u.getIdEmpresa()));
    }
}
