package sv.com.factuweb.api.dto;

import jakarta.validation.constraints.*;

/**
 * Cuenta de un vendedor o administrador.
 *
 * La contrasena solo se envia al crear la cuenta o al cambiarla; nunca se
 * devuelve al consultar.
 */
public class UsuarioDTO {

    private Long idUsuario;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato valido")
    private String email;

    @Size(min = 8, message = "La contrasena debe tener al menos 8 caracteres")
    private String password;

    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "ADMINISTRADOR|VENDEDOR", message = "El rol debe ser ADMINISTRADOR o VENDEDOR")
    private String rol;

    private Boolean activo;

    private Long facturasEmitidas;

    public UsuarioDTO() {
    }

    public UsuarioDTO(Long idUsuario,
                      String nombre,
                      String email,
                      String password,
                      String rol,
                      Boolean activo,
                      Long facturasEmitidas) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.activo = activo;
        this.facturasEmitidas = facturasEmitidas;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Long getFacturasEmitidas() {
        return facturasEmitidas;
    }

    public void setFacturasEmitidas(Long facturasEmitidas) {
        this.facturasEmitidas = facturasEmitidas;
    }

}
