package sv.com.factuweb.api.dto;

/**
 * Negocio tal como se muestra en la pagina publica.
 * No expone datos privados: solo el nombre, el rubro y dos totales.
 */
public class EmpresaPublicaDTO {

    private Long idEmpresa;

    private String nombre;

    private String rubro;

    private String iniciales;

    private String logoUrl;

    private long totalProductos;

    private long totalFacturas;

    public EmpresaPublicaDTO() {
    }

    public EmpresaPublicaDTO(Long idEmpresa,
                             String nombre,
                             String rubro,
                             String iniciales,
                             String logoUrl,
                             long totalProductos,
                             long totalFacturas) {
        this.idEmpresa = idEmpresa;
        this.nombre = nombre;
        this.rubro = rubro;
        this.iniciales = iniciales;
        this.logoUrl = logoUrl;
        this.totalProductos = totalProductos;
        this.totalFacturas = totalFacturas;
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

    public String getIniciales() {
        return iniciales;
    }

    public void setIniciales(String iniciales) {
        this.iniciales = iniciales;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public long getTotalProductos() {
        return totalProductos;
    }

    public void setTotalProductos(long totalProductos) {
        this.totalProductos = totalProductos;
    }

    public long getTotalFacturas() {
        return totalFacturas;
    }

    public void setTotalFacturas(long totalFacturas) {
        this.totalFacturas = totalFacturas;
    }

}
