package sv.com.factuweb.api.interfaces;

import sv.com.factuweb.api.dto.ClienteDTO;
import java.util.List;

/**
 * Clientes de un negocio. Es uno de los CRUD de tabla independiente.
 *
 * La interfaz declara QUE hace el servicio; la implementacion en el
 * paquete services dice COMO lo hace. Los controladores dependen de
 * esta interfaz, no de la clase concreta.
 */
public interface IClienteService {

    List<ClienteDTO> listar(Long idEmpresa);

    ClienteDTO obtener(Long idCliente, Long idEmpresa);

    ClienteDTO crear(ClienteDTO peticion, Long idEmpresa);

    ClienteDTO actualizar(Long idCliente, ClienteDTO peticion, Long idEmpresa);

    void eliminar(Long idCliente, Long idEmpresa);

}
