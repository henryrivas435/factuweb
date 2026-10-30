package sv.com.factuweb.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/** Datos del propio negocio: los que se imprimen en el encabezado de la factura. */
public class EmpresaDTO {

    private Long idEmpresa;

    @NotBlank(message = "El nombre del negocio es obligatorio")
    @Size(max = 100)
    private String nombre;

    @Size(max = 60)
    private String rubro;

    @Size(max = 20)
    private String nit;

    @Size(max = 150)
    private String direccion;

    @Size(max = 15)
    private String telefono;

    private String logoUrl;

    private LocalDateTime fechaRegistro;

    private Boolean activo;

    public EmpresaDTO() {
    }

    public EmpresaDTO(Long idEmpresa,
                      String nombre,
                      String rubro,
                      String nit,
                      String direccion,
                      String telefono,
                      String logoUrl,
                      LocalDateTime fechaRegistro,
                      Boolean activo) {
        this.idEmpresa = idEmpresa;
        this.nombre = nombre;
        this.rubro = rubro;
        this.nit = nit;
        this.direccion = direccion;
        this.telefono = telefono;
        this.logoUrl = logoUrl;
        this.fechaRegistro = fechaRegistro;
        this.activo = activo;
    }

    public Long getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRubro() {
        return rubro;
    }

    public void setRubro(String rubro) {
        this.rubro = rubro;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

}
