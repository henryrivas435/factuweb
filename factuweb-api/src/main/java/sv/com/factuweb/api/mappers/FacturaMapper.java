package sv.com.factuweb.api.mappers;

import org.springframework.stereotype.Component;
import sv.com.factuweb.api.dto.DetalleFacturaDTO;
import sv.com.factuweb.api.dto.FacturaDTO;
import sv.com.factuweb.api.dto.FacturaResumenDTO;
import sv.com.factuweb.api.entities.DetalleFactura;
import sv.com.factuweb.api.entities.Empresa;
import sv.com.factuweb.api.entities.Factura;

import java.util.List;

/** Convierte entre la entidad Factura y sus DTO. */
@Component
public class FacturaMapper {

    /** Fila del listado: sin el detalle, para que la consulta sea liviana. */
    public FacturaResumenDTO aResumen(Factura f) {
        if (f == null) return null;

        return new FacturaResumenDTO(
                f.getIdFactura(),
                f.getNumeroFactura(),
                f.getFechaEmision(),
                f.getNombre(),
                f.getUsuario() != null ? f.getUsuario().getNombre() : null,
                f.getTotal(),
                f.getEstado().name());
    }

    /** Factura completa, con su detalle y los datos del negocio emisor. */
    public FacturaDTO aDTO(Factura f) {
        if (f == null) return null;

        List<DetalleFacturaDTO> detalles = f.getDetalles().stream()
                .map(this::aDetalleDTO)
                .toList();

        Empresa e = f.getEmpresa();

        return new FacturaDTO(
                f.getIdFactura(), f.getNumeroFactura(), f.getFechaEmision(),
                f.getEstado().name(),
                e.getNombre(), e.getNit(), e.getDireccion(), e.getTelefono(), e.getLogoUrl(),
                f.getCliente() != null ? f.getCliente().getIdCliente() : null,
                f.getNombre(), f.getDui(), f.getDireccion(),
                f.getUsuario() != null ? f.getUsuario().getNombre() : null,
                f.getSubtotal(), f.getIva(), f.getTotal(), f.getQrUrl(),
                null,        // lineas: solo se usan al emitir
                detalles);
    }

    /**
     * Los datos del producto que se devuelven son los que se copiaron al
     * facturar, no los actuales del inventario.
     */
    public DetalleFacturaDTO aDetalleDTO(DetalleFactura d) {
        if (d == null) return null;

        return new DetalleFacturaDTO(
                d.getIdDetalle(),
                d.getProducto() != null ? d.getProducto().getIdProducto() : null,
                d.getItem(), d.getNombre(), d.getDescripcion(),
                d.getPrecio(), d.getCantidad(), d.getSubtotalLinea());
    }
}
