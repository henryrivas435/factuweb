package sv.com.factuweb.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Cliente de un negocio. Se usa tanto para crear como para consultar. */
public class ClienteDTO {

    private Long idCliente;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @Size(max = 20)
    private String duiNit;

    @Size(max = 150)
    private String direccion;

    @Size(max = 15)
    private String telefono;

    private Long facturasEmitidas;

    public ClienteDTO() {
    }

    public ClienteDTO(Long idCliente,
                      String nombre,
                      String duiNit,
                      String direccion,
                      String telefono,
                      Long facturasEmitidas) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.duiNit = duiNit;
        this.direccion = direccion;
        this.telefono = telefono;
        this.facturasEmitidas = facturasEmitidas;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDuiNit() {
        return duiNit;
    }

    public void setDuiNit(String duiNit) {
        this.duiNit = duiNit;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Long getFacturasEmitidas() {
        return facturasEmitidas;
    }

    public void setFacturasEmitidas(Long facturasEmitidas) {
        this.facturasEmitidas = facturasEmitidas;
    }

}
