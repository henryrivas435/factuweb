package sv.com.factuweb.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sv.com.factuweb.api.enums.EstadoFactura;
import sv.com.factuweb.api.entities.Factura;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FacturaRepository extends JpaRepository<Factura, Long> {

    List<Factura> findByEmpresaIdEmpresaOrderByFechaEmisionDesc(Long idEmpresa);

    Optional<Factura> findByIdFacturaAndEmpresaIdEmpresa(Long idFactura, Long idEmpresa);

    /** Consulta publica de verificacion del QR. */
    Optional<Factura> findByNumeroFactura(String numeroFactura);

    /**
     * Ultimo correlativo usado por el negocio. Se consulta dentro de la
     * transaccion de emision para calcular el siguiente numero.
     */
    @Query("SELECT COUNT(f) FROM Factura f WHERE f.empresa.idEmpresa = :idEmpresa")
    long contarPorEmpresa(@Param("idEmpresa") Long idEmpresa);

    /** Facturas de un periodo, filtradas por estado. */
    @Query("""
           SELECT f FROM Factura f
           WHERE f.empresa.idEmpresa = :idEmpresa
             AND f.fechaEmision BETWEEN :desde AND :hasta
             AND (:estado IS NULL OR f.estado = :estado)
           ORDER BY f.fechaEmision DESC
           """)
    List<Factura> buscarPorPeriodo(@Param("idEmpresa") Long idEmpresa,
                                   @Param("desde") LocalDateTime desde,
                                   @Param("hasta") LocalDateTime hasta,
                                   @Param("estado") EstadoFactura estado);

    /** Suma facturada en un periodo, contando solo las facturas emitidas. */
    @Query("""
           SELECT COALESCE(SUM(f.total), 0) FROM Factura f
           WHERE f.empresa.idEmpresa = :idEmpresa
             AND f.estado = sv.com.factuweb.api.enums.EstadoFactura.EMITIDA
             AND f.fechaEmision BETWEEN :desde AND :hasta
           """)
    BigDecimal sumarTotal(@Param("idEmpresa") Long idEmpresa,
                          @Param("desde") LocalDateTime desde,
                          @Param("hasta") LocalDateTime hasta);

    @Query("""
           SELECT COALESCE(SUM(f.iva), 0) FROM Factura f
           WHERE f.empresa.idEmpresa = :idEmpresa
             AND f.estado = sv.com.factuweb.api.enums.EstadoFactura.EMITIDA
             AND f.fechaEmision BETWEEN :desde AND :hasta
           """)
    BigDecimal sumarIva(@Param("idEmpresa") Long idEmpresa,
                        @Param("desde") LocalDateTime desde,
                        @Param("hasta") LocalDateTime hasta);

    @Query("""
           SELECT COUNT(f) FROM Factura f
           WHERE f.empresa.idEmpresa = :idEmpresa
             AND f.estado = sv.com.factuweb.api.enums.EstadoFactura.EMITIDA
             AND f.fechaEmision BETWEEN :desde AND :hasta
           """)
    long contarEmitidas(@Param("idEmpresa") Long idEmpresa,
                        @Param("desde") LocalDateTime desde,
                        @Param("hasta") LocalDateTime hasta);

    /** Ventas agrupadas por vendedor. Devuelve: nombre, cantidad, total. */
    @Query("""
           SELECT f.usuario.nombre, COUNT(f), COALESCE(SUM(f.total), 0)
           FROM Factura f
           WHERE f.empresa.idEmpresa = :idEmpresa
             AND f.estado = sv.com.factuweb.api.enums.EstadoFactura.EMITIDA
             AND f.fechaEmision BETWEEN :desde AND :hasta
           GROUP BY f.usuario.idUsuario, f.usuario.nombre
           ORDER BY SUM(f.total) DESC
           """)
    List<Object[]> ventasPorVendedor(@Param("idEmpresa") Long idEmpresa,
                                     @Param("desde") LocalDateTime desde,
                                     @Param("hasta") LocalDateTime hasta);

    /** Ultimas facturas para el panel. */
    List<Factura> findTop5ByEmpresaIdEmpresaOrderByFechaEmisionDesc(Long idEmpresa);

    /** Cuantas facturas ha emitido un vendedor. */
    long countByUsuarioIdUsuario(Long idUsuario);

    /** Cuantas facturas se le han emitido a un cliente. */
    long countByClienteIdCliente(Long idCliente);
}
