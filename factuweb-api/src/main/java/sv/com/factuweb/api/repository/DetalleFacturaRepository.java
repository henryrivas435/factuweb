package sv.com.factuweb.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sv.com.factuweb.api.entities.DetalleFactura;

import java.time.LocalDateTime;
import java.util.List;

public interface DetalleFacturaRepository extends JpaRepository<DetalleFactura, Long> {

    List<DetalleFactura> findByFacturaIdFactura(Long idFactura);

    /**
     * Productos mas vendidos en un periodo.
     * Devuelve: item, nombre, unidades vendidas, ingresos.
     */
    @Query("""
           SELECT d.item, d.nombre, COALESCE(SUM(d.cantidad), 0), COALESCE(SUM(d.subtotalLinea), 0)
           FROM DetalleFactura d
           WHERE d.factura.empresa.idEmpresa = :idEmpresa
             AND d.factura.estado = sv.com.factuweb.api.enums.EstadoFactura.EMITIDA
             AND d.factura.fechaEmision BETWEEN :desde AND :hasta
           GROUP BY d.item, d.nombre
           ORDER BY SUM(d.cantidad) DESC
           """)
    List<Object[]> masVendidos(@Param("idEmpresa") Long idEmpresa,
                               @Param("desde") LocalDateTime desde,
                               @Param("hasta") LocalDateTime hasta);

    @Query("""
           SELECT COALESCE(SUM(d.cantidad), 0)
           FROM DetalleFactura d
           WHERE d.factura.empresa.idEmpresa = :idEmpresa
             AND d.factura.estado = sv.com.factuweb.api.enums.EstadoFactura.EMITIDA
             AND d.factura.fechaEmision BETWEEN :desde AND :hasta
           """)
    Long sumarUnidades(@Param("idEmpresa") Long idEmpresa,
                       @Param("desde") LocalDateTime desde,
                       @Param("hasta") LocalDateTime hasta);
}
