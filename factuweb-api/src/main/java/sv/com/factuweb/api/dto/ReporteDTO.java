package sv.com.factuweb.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/** Resultado de un reporte de ventas sobre un periodo. */
public class ReporteDTO {

    private String tipoReporte;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private BigDecimal totalFacturado;

    private BigDecimal ivaRecaudado;

    private long unidadesVendidas;

    private long facturasEmitidas;

    private List<ProductoVendidoDTO> productosMasVendidos;

    private List<VentaVendedorDTO> ventasPorVendedor;

    public ReporteDTO() {
    }

    public ReporteDTO(String tipoReporte,
                      LocalDate fechaInicio,
                      LocalDate fechaFin,
                      BigDecimal totalFacturado,
                      BigDecimal ivaRecaudado,
                      long unidadesVendidas,
                      long facturasEmitidas,
                      List<ProductoVendidoDTO> productosMasVendidos,
                      List<VentaVendedorDTO> ventasPorVendedor) {
        this.tipoReporte = tipoReporte;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.totalFacturado = totalFacturado;
        this.ivaRecaudado = ivaRecaudado;
        this.unidadesVendidas = unidadesVendidas;
        this.facturasEmitidas = facturasEmitidas;
        this.productosMasVendidos = productosMasVendidos;
        this.ventasPorVendedor = ventasPorVendedor;
    }

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public BigDecimal getTotalFacturado() {
        return totalFacturado;
    }

    public void setTotalFacturado(BigDecimal totalFacturado) {
        this.totalFacturado = totalFacturado;
    }

    public BigDecimal getIvaRecaudado() {
        return ivaRecaudado;
    }

    public void setIvaRecaudado(BigDecimal ivaRecaudado) {
        this.ivaRecaudado = ivaRecaudado;
    }

    public long getUnidadesVendidas() {
        return unidadesVendidas;
    }

    public void setUnidadesVendidas(long unidadesVendidas) {
        this.unidadesVendidas = unidadesVendidas;
    }

    public long getFacturasEmitidas() {
        return facturasEmitidas;
    }

    public void setFacturasEmitidas(long facturasEmitidas) {
        this.facturasEmitidas = facturasEmitidas;
    }

    public List<ProductoVendidoDTO> getProductosMasVendidos() {
        return productosMasVendidos;
    }

    public void setProductosMasVendidos(List<ProductoVendidoDTO> productosMasVendidos) {
        this.productosMasVendidos = productosMasVendidos;
    }

    public List<VentaVendedorDTO> getVentasPorVendedor() {
        return ventasPorVendedor;
    }

    public void setVentasPorVendedor(List<VentaVendedorDTO> ventasPorVendedor) {
        this.ventasPorVendedor = ventasPorVendedor;
    }

}
