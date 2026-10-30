package sv.com.factuweb.api.interfaces;

import sv.com.factuweb.api.dto.UsuarioDTO;
import java.util.List;

/**
 * Cuentas de los vendedores de un negocio.
 *
 * La interfaz declara QUE hace el servicio; la implementacion en el
 * paquete services dice COMO lo hace. Los controladores dependen de
 * esta interfaz, no de la clase concreta.
 */
public interface IUsuarioService {

    List<UsuarioDTO> listar(Long idEmpresa);

    UsuarioDTO crear(UsuarioDTO peticion, Long idEmpresa);

    UsuarioDTO actualizar(Long idUsuario, UsuarioDTO peticion, Long idEmpresa);

    void desactivar(Long idUsuario, Long idEmpresa, Long idUsuarioActual);

    void reactivar(Long idUsuario, Long idEmpresa);

}
