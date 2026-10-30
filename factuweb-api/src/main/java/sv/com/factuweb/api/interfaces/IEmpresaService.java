package sv.com.factuweb.api.interfaces;

import sv.com.factuweb.api.dto.EmpresaDTO;
import sv.com.factuweb.api.dto.EmpresaPublicaDTO;
import java.util.List;

/**
 * Datos del propio negocio y listado publico de la plataforma.
 *
 * La interfaz declara QUE hace el servicio; la implementacion en el
 * paquete services dice COMO lo hace. Los controladores dependen de
 * esta interfaz, no de la clase concreta.
 */
public interface IEmpresaService {

    List<EmpresaPublicaDTO> listarPublicas();

    EmpresaDTO obtener(Long idEmpresa);

    EmpresaDTO actualizar(Long idEmpresa, EmpresaDTO datos);

}
