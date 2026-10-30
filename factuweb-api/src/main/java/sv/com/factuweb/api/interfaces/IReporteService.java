package sv.com.factuweb.api.interfaces;

import sv.com.factuweb.api.dto.*;
import sv.com.factuweb.api.repository.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Reportes de ventas e indicadores del panel.
 *
 * La interfaz declara QUE hace el servicio; la implementacion en el
 * paquete services dice COMO lo hace. Los controladores dependen de
 * esta interfaz, no de la clase concreta.
 */
public interface IReporteService {

    ReporteDTO generar(String tipoReporte, LocalDate desde, LocalDate hasta, Long idEmpresa, Long idUsuario);

    PanelDTO panel(Long idEmpresa);

    List<ReporteHistorialDTO> historial(Long idEmpresa);

}
