package sv.com.factuweb.api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** Fila del historial: constancia de un reporte que ya se genero. */
public class ReporteHistorialDTO {

    private Long idReporte;

    private String tipoReporte;

    private LocalDateTime fechaGeneracion;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private String generadoPor;

    public ReporteHistorialDTO() {
    }

    public ReporteHistorialDTO(Long idReporte,
                               String tipoReporte,
                               LocalDateTime fechaGeneracion,
                               LocalDate fechaInicio,
                               LocalDate fechaFin,
                               String generadoPor) {
        this.idReporte = idReporte;
        this.tipoReporte = tipoReporte;
        this.fechaGeneracion = fechaGeneracion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.generadoPor = generadoPor;
    }

    public Long getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(Long idReporte) {
        this.idReporte = idReporte;
    }

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDateTime fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getGeneradoPor() {
        return generadoPor;
    }

    public void setGeneradoPor(String generadoPor) {
        this.generadoPor = generadoPor;
    }

}
