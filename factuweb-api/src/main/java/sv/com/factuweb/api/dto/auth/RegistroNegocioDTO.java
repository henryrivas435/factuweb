package sv.com.factuweb.api.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Datos del formulario de registro: crean el negocio y su usuario
 * administrador en una sola operacion.
 */
public class RegistroNegocioDTO {

    @NotBlank(message = "El nombre del negocio es obligatorio")
    @Size(max = 100)
    private String nombreNegocio;

    @Size(max = 60)
    private String rubro;

    @Size(max = 20)
    private String nit;

    @Size(max = 150)
    private String direccion;

    @Size(max = 15)
    private String telefono;

    private String logoUrl;

    @NotBlank(message = "El nombre del administrador es obligatorio")
    @Size(max = 100)
    private String nombreAdmin;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato valido")
    private String emailAdmin;

    @NotBlank(message = "La contrasena es obligatoria")
    @Size(min = 8, message = "La contrasena debe tener al menos 8 caracteres")
    private String passwordAdmin;

    public RegistroNegocioDTO() {
    }

    public RegistroNegocioDTO(String nombreNegocio,
                              String rubro,
                              String nit,
                              String direccion,
                              String telefono,
                              String logoUrl,
                              String nombreAdmin,
                              String emailAdmin,
                              String passwordAdmin) {
        this.nombreNegocio = nombreNegocio;
        this.rubro = rubro;
        this.nit = nit;
        this.direccion = direccion;
        this.telefono = telefono;
        this.logoUrl = logoUrl;
        this.nombreAdmin = nombreAdmin;
        this.emailAdmin = emailAdmin;
        this.passwordAdmin = passwordAdmin;
    }

    public String getNombreNegocio() {
        return nombreNegocio;
    }

    public void setNombreNegocio(String nombreNegocio) {
        this.nombreNegocio = nombreNegocio;
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

    public String getNombreAdmin() {
        return nombreAdmin;
    }

    public void setNombreAdmin(String nombreAdmin) {
        this.nombreAdmin = nombreAdmin;
    }

    public String getEmailAdmin() {
        return emailAdmin;
    }

    public void setEmailAdmin(String emailAdmin) {
        this.emailAdmin = emailAdmin;
    }

    public String getPasswordAdmin() {
        return passwordAdmin;
    }

    public void setPasswordAdmin(String passwordAdmin) {
        this.passwordAdmin = passwordAdmin;
    }

}
