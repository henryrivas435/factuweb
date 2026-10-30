package sv.com.factuweb.api.interfaces;

import sv.com.factuweb.api.dto.*;
import sv.com.factuweb.api.entities.*;
import sv.com.factuweb.api.enums.*;
import sv.com.factuweb.api.repository.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Emision y consulta de facturas: la relacion maestro-detalle del sistema.
 *
 * La interfaz declara QUE hace el servicio; la implementacion en el
 * paquete services dice COMO lo hace. Los controladores dependen de
 * esta interfaz, no de la clase concreta.
 */
public interface IFacturaService {

    FacturaDTO issue(FacturaDTO peticion, Long idEmpresa, Long idUsuario);

    List<FacturaResumenDTO> list(Long idEmpresa);

    List<FacturaResumenDTO> findByPeriod(Long idEmpresa, LocalDate desde, LocalDate hasta, EstadoFactura estado);

    FacturaDTO get(Long idFactura, Long idEmpresa);

    FacturaDTO verify(String numeroFactura);

    FacturaDTO cancel(Long idFactura, Long idEmpresa);

}
