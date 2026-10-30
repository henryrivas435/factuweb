package sv.com.factuweb.api.dto;

import java.math.BigDecimal;

/** Fila del reporte de ventas por vendedor. */
public class VentaVendedorDTO {

    private String vendedor;

    private long facturas;

    private BigDecimal total;

    public VentaVendedorDTO() {
    }

    public VentaVendedorDTO(String vendedor, long facturas, BigDecimal total) {
        this.vendedor = vendedor;
        this.facturas = facturas;
        this.total = total;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public long getFacturas() {
        return facturas;
    }

    public void setFacturas(long facturas) {
        this.facturas = facturas;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

}
