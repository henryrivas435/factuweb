package sv.com.factuweb.api.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.com.factuweb.api.dto.*;
import sv.com.factuweb.api.entities.Empresa;
import sv.com.factuweb.api.entities.Reporte;
import sv.com.factuweb.api.entities.Usuario;
import sv.com.factuweb.api.exception.RecursoNoEncontradoException;
import sv.com.factuweb.api.exception.ReglaNegocioException;
import sv.com.factuweb.api.interfaces.IReporteService;
import sv.com.factuweb.api.mappers.FacturaMapper;
import sv.com.factuweb.api.mappers.ReporteMapper;
import sv.com.factuweb.api.repository.*;

/**
 * Reportes de ventas.
 *
 * Los datos se calculan al vuelo sobre FACTURA y DETALLE_FACTURA. La tabla
 * REPORTE no guarda los resultados: guarda la constancia de quien pidio el
 * reporte, de que tipo y sobre que periodo.
 */
@Service
public class ReporteService implements IReporteService {

    private final FacturaRepository facturaRepository;
    private final FacturaMapper facturaMapper;
    private final ReporteMapper reporteMapper;
    private final DetalleFacturaRepository detalleRepository;
    private final ReporteRepository reporteRepository;
    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    public ReporteService(FacturaRepository facturaRepository,
                           DetalleFacturaRepository detalleRepository,
                           ReporteRepository reporteRepository,
                           EmpresaRepository empresaRepository,
                           UsuarioRepository usuarioRepository,
                           ProductoRepository productoRepository,
                           ReporteMapper reporteMapper,
                           FacturaMapper facturaMapper) {
        this.facturaRepository = facturaRepository;
        this.detalleRepository = detalleRepository;
        this.reporteRepository = reporteRepository;
        this.empresaRepository = empresaRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.reporteMapper = reporteMapper;
        this.facturaMapper = facturaMapper;
    }

    /** Genera el reporte y deja constancia en la tabla REPORTE. */
    @Transactional
    @Override
    public ReporteDTO generar(String tipoReporte, LocalDate desde, LocalDate hasta,
                              Long idEmpresa, Long idUsuario) {

        // Sin esta validacion, un rango invertido no falla: simplemente
        // devuelve todo en cero, y el usuario cree que no hubo ventas.
        if (desde == null || hasta == null) {
            throw new ReglaNegocioException("Debe indicar la fecha de inicio y la fecha final");
        }
        if (desde.isAfter(hasta)) {
            throw new ReglaNegocioException(
                    "La fecha de inicio no puede ser posterior a la fecha final");
        }

        LocalDateTime inicio = desde.atStartOfDay();
        LocalDateTime fin = hasta.atTime(23, 59, 59);

        BigDecimal totalFacturado = facturaRepository.sumarTotal(idEmpresa, inicio, fin);
        BigDecimal ivaRecaudado = facturaRepository.sumarIva(idEmpresa, inicio, fin);
        long facturasEmitidas = facturaRepository.contarEmitidas(idEmpresa, inicio, fin);
        Long unidades = detalleRepository.sumarUnidades(idEmpresa, inicio, fin);

        List<ProductoVendidoDTO> masVendidos =
                detalleRepository.masVendidos(idEmpresa, inicio, fin).stream()
                        .map(f -> new ProductoVendidoDTO(
                                (String) f[0],
                                (String) f[1],
                                ((Number) f[2]).longValue(),
                                (BigDecimal) f[3]))
                        .toList();

        List<VentaVendedorDTO> porVendedor =
                facturaRepository.ventasPorVendedor(idEmpresa, inicio, fin).stream()
                        .map(f -> new VentaVendedorDTO(
                                (String) f[0],
                                ((Number) f[1]).longValue(),
                                (BigDecimal) f[2]))
                        .toList();

        registrar(tipoReporte, desde, hasta, idEmpresa, idUsuario);

        return new ReporteDTO(
                tipoReporte, desde, hasta,
                totalFacturado, ivaRecaudado,
                unidades == null ? 0 : unidades,
                facturasEmitidas,
                masVendidos, porVendedor);
    }

    /** Indicadores del panel del administrador. */
    @Transactional(readOnly = true)
    @Override
    public PanelDTO panel(Long idEmpresa) {
        LocalDate hoy = LocalDate.now();
        LocalDateTime inicioDia = hoy.atStartOfDay();
        LocalDateTime finDia = hoy.atTime(23, 59, 59);
        LocalDateTime inicioMes = hoy.withDayOfMonth(1).atStartOfDay();

        List<FacturaResumenDTO> ultimas =
                facturaRepository.findTop5ByEmpresaIdEmpresaOrderByFechaEmisionDesc(idEmpresa)
                        .stream()
                        .map(facturaMapper::aResumen)
                        .toList();

        return new PanelDTO(
                facturaRepository.sumarTotal(idEmpresa, inicioDia, finDia),
                facturaRepository.contarEmitidas(idEmpresa, inicioDia, finDia),
                facturaRepository.sumarTotal(idEmpresa, inicioMes, finDia),
                facturaRepository.contarEmitidas(idEmpresa, inicioMes, finDia),
                usuarioRepository.countByEmpresaIdEmpresaAndActivoTrue(idEmpresa),
                productoRepository.contarBajoMinimo(idEmpresa),
                ultimas);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReporteHistorialDTO> historial(Long idEmpresa) {
        return reporteRepository.findTop20ByEmpresaIdEmpresaOrderByFechaGeneracionDesc(idEmpresa)
                .stream()
                .map(reporteMapper::aDTO)
                .toList();
    }

    private void registrar(String tipo, LocalDate desde, LocalDate hasta,
                           Long idEmpresa, Long idUsuario) {
        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new RecursoNoEncontradoException("Negocio no encontrado"));
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        Reporte reporte = new Reporte();
        reporte.setEmpresa(empresa);
        reporte.setUsuario(usuario);
        reporte.setTipoReporte(tipo);
        reporte.setFechaInicio(desde);
        reporte.setFechaFin(hasta);
        reporte.setFechaGeneracion(LocalDateTime.now());
        reporteRepository.save(reporte);
    }
}
