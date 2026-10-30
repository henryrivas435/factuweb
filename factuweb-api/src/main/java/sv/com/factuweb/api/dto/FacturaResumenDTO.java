package sv.com.factuweb.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Fila del listado de facturas: no incluye el detalle. */
public class FacturaResumenDTO {

    private Long idFactura;

    private String numeroFactura;

    private LocalDateTime fechaEmision;

    private String clienteNombre;

    private String vendedor;

    private BigDecimal total;

    private String estado;

    public FacturaResumenDTO() {
    }

    public FacturaResumenDTO(Long idFactura,
                             String numeroFactura,
                             LocalDateTime fechaEmision,
                             String clienteNombre,
                             String vendedor,
                             BigDecimal total,
                             String estado) {
        this.idFactura = idFactura;
        this.numeroFactura = numeroFactura;
        this.fechaEmision = fechaEmision;
        this.clienteNombre = clienteNombre;
        this.vendedor = vendedor;
        this.total = total;
        this.estado = estado;
    }

    public Long getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Long idFactura) {
        this.idFactura = idFactura;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
