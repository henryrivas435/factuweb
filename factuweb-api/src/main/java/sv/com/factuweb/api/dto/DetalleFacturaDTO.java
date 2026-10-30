package sv.com.factuweb.api.dto;

import java.math.BigDecimal;

/**
 * Linea ya guardada de una factura. Los datos del producto son los que se
 * copiaron al momento de la venta, no los actuales.
 */
public class DetalleFacturaDTO {

    private Long idDetalle;

    private Long idProducto;

    private String item;

    private String nombre;

    private String descripcion;

    private BigDecimal precio;

    private Integer cantidad;

    private BigDecimal subtotalLinea;

    public DetalleFacturaDTO() {
    }

    public DetalleFacturaDTO(Long idDetalle,
                             Long idProducto,
                             String item,
                             String nombre,
                             String descripcion,
                             BigDecimal precio,
                             Integer cantidad,
                             BigDecimal subtotalLinea) {
        this.idDetalle = idDetalle;
        this.idProducto = idProducto;
        this.item = item;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidad = cantidad;
        this.subtotalLinea = subtotalLinea;
    }

    public Long getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Long idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getSubtotalLinea() {
        return subtotalLinea;
    }

    public void setSubtotalLinea(BigDecimal subtotalLinea) {
        this.subtotalLinea = subtotalLinea;
    }

}
