package sv.com.factuweb.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sv.com.factuweb.api.entities.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /** Solo los productos activos del negocio: es el listado normal del stock. */
    List<Producto> findByEmpresaIdEmpresaAndActivoTrueOrderByNombreAsc(Long idEmpresa);

    /** Incluye tambien los inactivos, para el filtro del administrador. */
    List<Producto> findByEmpresaIdEmpresaOrderByNombreAsc(Long idEmpresa);

    Optional<Producto> findByIdProductoAndEmpresaIdEmpresa(Long idProducto, Long idEmpresa);

    /** Busqueda por nombre o codigo de item dentro del negocio. */
    @Query("""
           SELECT p FROM Producto p
           WHERE p.empresa.idEmpresa = :idEmpresa
             AND p.activo = true
             AND (LOWER(p.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
                  OR LOWER(p.item) LIKE LOWER(CONCAT('%', :texto, '%')))
           ORDER BY p.nombre
           """)
    List<Producto> buscar(@Param("idEmpresa") Long idEmpresa, @Param("texto") String texto);

    /** Productos que llegaron a su stock minimo. Alimenta la alerta del panel. */
    @Query("""
           SELECT p FROM Producto p
           WHERE p.empresa.idEmpresa = :idEmpresa
             AND p.activo = true
             AND p.stock <= p.stockMinimo
           ORDER BY p.stock
           """)
    List<Producto> bajoMinimo(@Param("idEmpresa") Long idEmpresa);

    @Query("""
           SELECT COUNT(p) FROM Producto p
           WHERE p.empresa.idEmpresa = :idEmpresa
             AND p.activo = true
             AND p.stock <= p.stockMinimo
           """)
    long contarBajoMinimo(@Param("idEmpresa") Long idEmpresa);

    boolean existsByEmpresaIdEmpresaAndItem(Long idEmpresa, String item);

    /** Para no borrar una categoria que todavia tiene productos asignados. */
    boolean existsByCategoriaIdCategoria(Long idCategoria);
}
