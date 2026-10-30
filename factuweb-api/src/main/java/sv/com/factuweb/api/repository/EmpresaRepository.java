package sv.com.factuweb.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sv.com.factuweb.api.entities.Empresa;

import java.util.List;
import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    List<Empresa> findByActivoTrueOrderByNombreAsc();

    Optional<Empresa> findByNit(String nit);

    boolean existsByNit(String nit);

    /** Cantidad de productos activos de un negocio (para la vista publica). */
    @Query("SELECT COUNT(p) FROM Producto p WHERE p.empresa.idEmpresa = :idEmpresa AND p.activo = true")
    long contarProductos(Long idEmpresa);

    /** Cantidad de facturas emitidas de un negocio (para la vista publica). */
    @Query("SELECT COUNT(f) FROM Factura f WHERE f.empresa.idEmpresa = :idEmpresa AND f.estado = sv.com.factuweb.api.enums.EstadoFactura.EMITIDA")
    long contarFacturas(Long idEmpresa);
}
