package sv.com.factuweb.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.com.factuweb.api.entities.Reporte;

import java.util.List;

public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    List<Reporte> findTop20ByEmpresaIdEmpresaOrderByFechaGeneracionDesc(Long idEmpresa);
}
