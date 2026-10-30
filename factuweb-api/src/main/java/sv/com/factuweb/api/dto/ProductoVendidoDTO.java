package sv.com.factuweb.api.dto;

import java.math.BigDecimal;

/** Fila del reporte de productos mas vendidos. */
public class ProductoVendidoDTO {

    private String item;

    private String nombre;

    private long unidades;

    private BigDecimal ingresos;

    public ProductoVendidoDTO() {
    }

    public ProductoVendidoDTO(String item, String nombre, long unidades, BigDecimal ingresos) {
        this.item = item;
        this.nombre = nombre;
        this.unidades = unidades;
        this.ingresos = ingresos;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getUnidades() {
        return unidades;
    }

    public void setUnidades(long unidades) {
        this.unidades = unidades;
    }

    public BigDecimal getIngresos() {
        return ingresos;
    }

    public void setIngresos(BigDecimal ingresos) {
        this.ingresos = ingresos;
    }

}
