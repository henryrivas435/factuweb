package sv.com.factuweb.api.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import sv.com.factuweb.api.enums.Rol;
import sv.com.factuweb.api.entities.Usuario;

import java.util.Collection;
import java.util.List;

/**
 * Usuario autenticado.
 *
 * Ademas de los datos que exige Spring Security, expone el idEmpresa: es el
 * dato con el que todos los servicios filtran la informacion, de modo que un
 * negocio nunca pueda leer ni modificar datos de otro.
 */
public class UsuarioPrincipal implements UserDetails {

    private final Long idUsuario;
    private final Long idEmpresa;
    private final String nombre;
    private final String email;
    private final String password;
    private final Rol rol;
    private final boolean activo;

    public UsuarioPrincipal(Usuario usuario) {
        this.idUsuario = usuario.getIdUsuario();
        this.idEmpresa = usuario.getEmpresa().getIdEmpresa();
        this.nombre = usuario.getNombre();
        this.email = usuario.getEmail();
        this.password = usuario.getPassword();
        this.rol = usuario.getRol();
        this.activo = Boolean.TRUE.equals(usuario.getActivo());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return activo; }

    public Long getIdUsuario() { return idUsuario; }
    public Long getIdEmpresa() { return idEmpresa; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public Rol getRol() { return rol; }

    public boolean esAdministrador() { return rol == Rol.ADMINISTRADOR; }
}
