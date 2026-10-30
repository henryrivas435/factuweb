package sv.com.factuweb.api.interfaces;

import sv.com.factuweb.api.dto.auth.LoginRequestDTO;
import sv.com.factuweb.api.dto.auth.LoginResponseDTO;
import sv.com.factuweb.api.dto.auth.RegistroNegocioDTO;

/**
 * Inicio de sesion y registro de negocios nuevos.
 *
 * La interfaz declara QUE hace el servicio; la implementacion en el
 * paquete services dice COMO lo hace. Los controladores dependen de
 * esta interfaz, no de la clase concreta.
 */
public interface IAuthService {

    LoginResponseDTO iniciarSesion(LoginRequestDTO peticion);

    LoginResponseDTO registrarNegocio(RegistroNegocioDTO peticion);

}
