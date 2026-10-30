package sv.com.factuweb.api.dto.auth;

/** Token y datos de la sesion que se devuelven al iniciar sesion. */
public class LoginResponseDTO {

    private String token;

    private long expiraEnMs;

    private Long idUsuario;

    private String nombre;

    private String email;

    private String rol;

    private Long idEmpresa;

    private String nombreEmpresa;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String token,
                            long expiraEnMs,
                            Long idUsuario,
                            String nombre,
                            String email,
                            String rol,
                            Long idEmpresa,
                            String nombreEmpresa) {
        this.token = token;
        this.expiraEnMs = expiraEnMs;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.idEmpresa = idEmpresa;
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiraEnMs() {
        return expiraEnMs;
    }

    public void setExpiraEnMs(long expiraEnMs) {
        this.expiraEnMs = expiraEnMs;
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Long getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

}
