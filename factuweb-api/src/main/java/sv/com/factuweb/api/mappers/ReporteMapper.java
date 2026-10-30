package sv.com.factuweb.api.mappers;

import org.springframework.stereotype.Component;
import sv.com.factuweb.api.dto.ReporteHistorialDTO;
import sv.com.factuweb.api.entities.Reporte;

/** Convierte la entidad Reporte a su DTO de historial. */
@Component
public class ReporteMapper {

    public ReporteHistorialDTO aDTO(Reporte r) {
        if (r == null) return null;

        return new ReporteHistorialDTO(
                r.getIdReporte(),
                r.getTipoReporte(),
                r.getFechaGeneracion(),
                r.getFechaInicio(),
                r.getFechaFin(),
                r.getUsuario() != null ? r.getUsuario().getNombre() : null);
    }
}
