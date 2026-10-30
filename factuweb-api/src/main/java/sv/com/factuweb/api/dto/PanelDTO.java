package sv.com.factuweb.api.dto;

import java.math.BigDecimal;
import java.util.List;

/** Indicadores del panel del administrador. */
public class PanelDTO {

    private BigDecimal ventasHoy;

    private long facturasHoy;

    private BigDecimal ventasMes;

    private long facturasMes;

    private long vendedoresActivos;

    private long productosBajoMinimo;

    private List<FacturaResumenDTO> ultimasFacturas;

    public PanelDTO() {
    }

    public PanelDTO(BigDecimal ventasHoy,
                    long facturasHoy,
                    BigDecimal ventasMes,
                    long facturasMes,
                    long vendedoresActivos,
                    long productosBajoMinimo,
                    List<FacturaResumenDTO> ultimasFacturas) {
        this.ventasHoy = ventasHoy;
        this.facturasHoy = facturasHoy;
        this.ventasMes = ventasMes;
        this.facturasMes = facturasMes;
        this.vendedoresActivos = vendedoresActivos;
        this.productosBajoMinimo = productosBajoMinimo;
        this.ultimasFacturas = ultimasFacturas;
    }

    public BigDecimal getVentasHoy() {
        return ventasHoy;
    }

    public void setVentasHoy(BigDecimal ventasHoy) {
        this.ventasHoy = ventasHoy;
    }

    public long getFacturasHoy() {
        return facturasHoy;
    }

    public void setFacturasHoy(long facturasHoy) {
        this.facturasHoy = facturasHoy;
    }

    public BigDecimal getVentasMes() {
        return ventasMes;
    }

    public void setVentasMes(BigDecimal ventasMes) {
        this.ventasMes = ventasMes;
    }

    public long getFacturasMes() {
        return facturasMes;
    }

    public void setFacturasMes(long facturasMes) {
        this.facturasMes = facturasMes;
    }

    public long getVendedoresActivos() {
        return vendedoresActivos;
    }

    public void setVendedoresActivos(long vendedoresActivos) {
        this.vendedoresActivos = vendedoresActivos;
    }

    public long getProductosBajoMinimo() {
        return productosBajoMinimo;
    }

    public void setProductosBajoMinimo(long productosBajoMinimo) {
        this.productosBajoMinimo = productosBajoMinimo;
    }

    public List<FacturaResumenDTO> getUltimasFacturas() {
        return ultimasFacturas;
    }

    public void setUltimasFacturas(List<FacturaResumenDTO> ultimasFacturas) {
        this.ultimasFacturas = ultimasFacturas;
    }

}
