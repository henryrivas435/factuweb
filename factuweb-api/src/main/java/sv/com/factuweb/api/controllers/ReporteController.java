package sv.com.factuweb.api.controllers;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sv.com.factuweb.api.dto.PanelDTO;
import sv.com.factuweb.api.dto.ReporteDTO;
import sv.com.factuweb.api.dto.ReporteHistorialDTO;
import sv.com.factuweb.api.security.UsuarioPrincipal;
import sv.com.factuweb.api.interfaces.IReporteService;

import java.time.LocalDate;
import java.util.List;

/** Panel y reportes. Solo accesibles para el administrador del negocio. */
@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class ReporteController {

    private final IReporteService reporteService;

    public ReporteController(IReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/panel")
    public ResponseEntity<PanelDTO> panel(@AuthenticationPrincipal UsuarioPrincipal u) {
        return ResponseEntity.ok(reporteService.panel(u.getIdEmpresa()));
    }

    @GetMapping("/reportes")
    public ResponseEntity<ReporteDTO> generar(
            @RequestParam(defaultValue = "ventas_por_periodo") String tipo,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @AuthenticationPrincipal UsuarioPrincipal u) {
        return ResponseEntity.ok(
                reporteService.generar(tipo, desde, hasta, u.getIdEmpresa(), u.getIdUsuario()));
    }

    @GetMapping("/reportes/historial")
    public ResponseEntity<List<ReporteHistorialDTO>> historial(
            @AuthenticationPrincipal UsuarioPrincipal u) {
        return ResponseEntity.ok(reporteService.historial(u.getIdEmpresa()));
    }
}
