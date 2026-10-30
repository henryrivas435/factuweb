package sv.com.factuweb.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Factura completa.
 *
 * Al emitir, el frontend envia idCliente y lineas; si idCliente viene vacio
 * se factura a consumidor final y se usan los campos clienteNombre,
 * clienteDui y clienteDireccion escritos a mano.
 *
 * Al consultar, se devuelven todos los campos junto con el detalle.
 */
public class FacturaDTO {

    private Long idFactura;

    private String numeroFactura;

    private LocalDateTime fechaEmision;

    private String estado;

    private String empresaNombre;

    private String empresaNit;

    private String empresaDireccion;

    private String empresaTelefono;

    private String empresaLogoUrl;

    private Long idCliente;

    private String clienteNombre;

    private String clienteDui;

    private String clienteDireccion;

    private String vendedor;

    private BigDecimal subtotal;

    private BigDecimal iva;

    private BigDecimal total;

    private String qrUrl;

    @NotEmpty(message = "La factura debe tener al menos un producto")
    @Valid
    private List<LineaFacturaDTO> lineas;

    private List<DetalleFacturaDTO> detalles;

    public FacturaDTO() {
    }

    public FacturaDTO(Long idFactura,
                      String numeroFactura,
                      LocalDateTime fechaEmision,
                      String estado,
                      String empresaNombre,
                      String empresaNit,
                      String empresaDireccion,
                      String empresaTelefono,
                      String empresaLogoUrl,
                      Long idCliente,
                      String clienteNombre,
                      String clienteDui,
                      String clienteDireccion,
                      String vendedor,
                      BigDecimal subtotal,
                      BigDecimal iva,
                      BigDecimal total,
                      String qrUrl,
                      List<LineaFacturaDTO> lineas,
                      List<DetalleFacturaDTO> detalles) {
        this.idFactura = idFactura;
        this.numeroFactura = numeroFactura;
        this.fechaEmision = fechaEmision;
        this.estado = estado;
        this.empresaNombre = empresaNombre;
        this.empresaNit = empresaNit;
        this.empresaDireccion = empresaDireccion;
        this.empresaTelefono = empresaTelefono;
        this.empresaLogoUrl = empresaLogoUrl;
        this.idCliente = idCliente;
        this.clienteNombre = clienteNombre;
        this.clienteDui = clienteDui;
        this.clienteDireccion = clienteDireccion;
        this.vendedor = vendedor;
        this.subtotal = subtotal;
        this.iva = iva;
        this.total = total;
        this.qrUrl = qrUrl;
        this.lineas = lineas;
        this.detalles = detalles;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEmpresaNombre() {
        return empresaNombre;
    }

    public void setEmpresaNombre(String empresaNombre) {
        this.empresaNombre = empresaNombre;
    }

    public String getEmpresaNit() {
        return empresaNit;
    }

    public void setEmpresaNit(String empresaNit) {
        this.empresaNit = empresaNit;
    }

    public String getEmpresaDireccion() {
        return empresaDireccion;
    }

    public void setEmpresaDireccion(String empresaDireccion) {
        this.empresaDireccion = empresaDireccion;
    }

    public String getEmpresaTelefono() {
        return empresaTelefono;
    }

    public void setEmpresaTelefono(String empresaTelefono) {
        this.empresaTelefono = empresaTelefono;
    }

    public String getEmpresaLogoUrl() {
        return empresaLogoUrl;
    }

    public void setEmpresaLogoUrl(String empresaLogoUrl) {
        this.empresaLogoUrl = empresaLogoUrl;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getClienteDui() {
        return clienteDui;
    }

    public void setClienteDui(String clienteDui) {
        this.clienteDui = clienteDui;
    }

    public String getClienteDireccion() {
        return clienteDireccion;
    }

    public void setClienteDireccion(String clienteDireccion) {
        this.clienteDireccion = clienteDireccion;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getQrUrl() {
        return qrUrl;
    }

    public void setQrUrl(String qrUrl) {
        this.qrUrl = qrUrl;
    }

    public List<LineaFacturaDTO> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaFacturaDTO> lineas) {
        this.lineas = lineas;
    }

    public List<DetalleFacturaDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleFacturaDTO> detalles) {
        this.detalles = detalles;
    }

}
