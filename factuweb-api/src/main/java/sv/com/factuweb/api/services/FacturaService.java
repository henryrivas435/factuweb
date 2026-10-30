package sv.com.factuweb.api.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.com.factuweb.api.dto.*;
import sv.com.factuweb.api.entities.*;
import sv.com.factuweb.api.enums.*;
import sv.com.factuweb.api.exception.RecursoNoEncontradoException;
import sv.com.factuweb.api.exception.ReglaNegocioException;
import sv.com.factuweb.api.interfaces.IFacturaService;
import sv.com.factuweb.api.mappers.FacturaMapper;
import sv.com.factuweb.api.repository.*;

/**
 * Proceso de facturacion. Es la transaccion principal del sistema.
 *
 * Al emitir una factura ocurren cuatro cosas dentro de una sola transaccion:
 *   1. Se calcula el numero correlativo del negocio.
 *   2. Se copian los datos del cliente y de cada producto a la factura.
 *   3. Se calculan subtotal, IVA y total.
 *   4. Se descuenta el stock de cada producto vendido.
 *
 * Si cualquiera de esos pasos falla, la anotacion @Transactional revierte
 * todo: no queda una factura a medias ni un stock descuadrado.
 */
@Service
public class FacturaService implements IFacturaService {

    private final FacturaRepository facturaRepository;
    private final FacturaMapper facturaMapper;
    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;

    private final BigDecimal porcentajeIva;
    private final String urlVerificacion;

    public FacturaService(FacturaRepository facturaRepository,
                          ProductoRepository productoRepository,
                          ClienteRepository clienteRepository,
                          UsuarioRepository usuarioRepository,
                          EmpresaRepository empresaRepository,
                          FacturaMapper facturaMapper,
                          @Value("${factuweb.facturacion.porcentaje-iva}") BigDecimal porcentajeIva,
                          @Value("${factuweb.facturacion.url-verificacion}") String urlVerificacion) {
        this.facturaRepository = facturaRepository;
        this.productoRepository = productoRepository;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.empresaRepository = empresaRepository;
        this.porcentajeIva = porcentajeIva;
        this.urlVerificacion = urlVerificacion;
        this.facturaMapper = facturaMapper;
    }

    /* ==================== EMISION ==================== */

    @Transactional
    @Override
    public FacturaDTO issue(FacturaDTO peticion, Long idEmpresa, Long idUsuario) {

        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new RecursoNoEncontradoException("Negocio no encontrado"));
        Usuario usuario = usuarioRepository.findByIdUsuarioAndEmpresaIdEmpresa(idUsuario, idEmpresa)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        Factura factura = new Factura();
        factura.setEmpresa(empresa);
        factura.setUsuario(usuario);
        factura.setEstado(EstadoFactura.EMITIDA);
        factura.setFechaEmision(LocalDateTime.now());
        factura.setNumeroFactura(nextSequentialNumber(idEmpresa));

        // Paso 2a: copiar los datos del cliente a la factura.
        if (peticion.getIdCliente() != null) {
            Cliente cliente = clienteRepository
                    .findByIdClienteAndEmpresaIdEmpresa(peticion.getIdCliente(), idEmpresa)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "El cliente indicado no pertenece a este negocio"));
            factura.setCliente(cliente);
            factura.setNombre(cliente.getNombre());
            factura.setDui(cliente.getDuiNit());
            factura.setDireccion(cliente.getDireccion());
        } else {
            factura.setCliente(null);
            factura.setNombre(peticion.getClienteNombre() == null
                    || peticion.getClienteNombre().isBlank()
                    ? "Consumidor final" : peticion.getClienteNombre());
            factura.setDui(peticion.getClienteDui());
            factura.setDireccion(peticion.getClienteDireccion());
        }

        BigDecimal subtotal = BigDecimal.ZERO;

        for (LineaFacturaDTO linea : peticion.getLineas()) {
            Producto producto = productoRepository
                    .findByIdProductoAndEmpresaIdEmpresa(linea.getIdProducto(), idEmpresa)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "El producto con id " + linea.getIdProducto()
                            + " no pertenece a este negocio"));

            if (!Boolean.TRUE.equals(producto.getActivo())) {
                throw new ReglaNegocioException(
                        "El producto " + producto.getNombre() + " esta inactivo");
            }
            if (producto.getStock() < linea.getCantidad()) {
                throw new ReglaNegocioException(
                        "Stock insuficiente de " + producto.getNombre()
                        + ". Disponible: " + producto.getStock()
                        + ", solicitado: " + linea.getCantidad());
            }

            // Paso 2b: copiar los datos del producto a la linea de detalle.
            DetalleFactura detalle = new DetalleFactura();
            detalle.setProducto(producto);
            detalle.setItem(producto.getItem());
            detalle.setNombre(producto.getNombre());
            detalle.setDescripcion(producto.getDescripcion());
            detalle.setPrecio(producto.getPrecio());
            detalle.setCantidad(linea.getCantidad());

            BigDecimal subtotalLinea = producto.getPrecio()
                    .multiply(BigDecimal.valueOf(linea.getCantidad()))
                    .setScale(2, RoundingMode.HALF_UP);
            detalle.setSubtotalLinea(subtotalLinea);

            factura.agregarDetalle(detalle);
            subtotal = subtotal.add(subtotalLinea);

            // Paso 4: descontar el stock.
            producto.setStock(producto.getStock() - linea.getCantidad());
            productoRepository.save(producto);
        }

        // Paso 3: calcular los totales.
        subtotal = subtotal.setScale(2, RoundingMode.HALF_UP);
        BigDecimal iva = subtotal.multiply(porcentajeIva).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(iva).setScale(2, RoundingMode.HALF_UP);

        factura.setSubtotal(subtotal);
        factura.setIva(iva);
        factura.setTotal(total);
        factura.setQrUrl(urlVerificacion + factura.getNumeroFactura());

        factura = facturaRepository.save(factura);
        return facturaMapper.aDTO(factura);
    }

    /**
     * Calcula el siguiente numero correlativo del negocio.
     * El formato es de seis digitos: 000001, 000002, etc.
     */
    private String nextSequentialNumber(Long idEmpresa) {
        long emitidas = facturaRepository.contarPorEmpresa(idEmpresa);
        return String.format("%06d", emitidas + 1);
    }

    /* CONSULTAS */

    @Transactional(readOnly = true)
    @Override
    public List<FacturaResumenDTO> list(Long idEmpresa) {
        return facturaRepository.findByEmpresaIdEmpresaOrderByFechaEmisionDesc(idEmpresa)
                .stream().map(facturaMapper::aResumen).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<FacturaResumenDTO> findByPeriod(Long idEmpresa, LocalDate desde,
                                                    LocalDate hasta, EstadoFactura estado) {
        return facturaRepository.buscarPorPeriodo(idEmpresa,
                        desde.atStartOfDay(), hasta.atTime(23, 59, 59), estado)
                .stream().map(facturaMapper::aResumen).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public FacturaDTO get(Long idFactura, Long idEmpresa) {
        Factura factura = facturaRepository
                .findByIdFacturaAndEmpresaIdEmpresa(idFactura, idEmpresa)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro la factura con id " + idFactura));
        return facturaMapper.aDTO(factura);
    }

    /** Consulta publica: la que se abre al escanear el codigo QR. */
    @Transactional(readOnly = true)
    @Override
    public FacturaDTO verify(String numeroFactura) {
        Factura factura = facturaRepository.findByNumeroFactura(numeroFactura)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la factura numero " + numeroFactura));
        return facturaMapper.aDTO(factura);
    }

    /* CAMBIOS DE ESTADO */

    /**
     * Anula una factura. No se borra la fila: solo cambia el estado, para
     * conservar la numeracion correlativa. El stock se devuelve al inventario.
     */
    @Transactional
    @Override
    public FacturaDTO cancel(Long idFactura, Long idEmpresa) {
        Factura factura = facturaRepository
                .findByIdFacturaAndEmpresaIdEmpresa(idFactura, idEmpresa)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro la factura con id " + idFactura));

        if (factura.getEstado() != EstadoFactura.EMITIDA) {
            throw new ReglaNegocioException(
                    "Solo se pueden anular las facturas en estado EMITIDA");
        }

        for (DetalleFactura detalle : factura.getDetalles()) {
            Producto producto = detalle.getProducto();
            if (producto != null) {
                producto.setStock(producto.getStock() + detalle.getCantidad());
                productoRepository.save(producto);
            }
        }

        factura.setEstado(EstadoFactura.ANULADA);
        return facturaMapper.aDTO(facturaRepository.save(factura));
    }

}
