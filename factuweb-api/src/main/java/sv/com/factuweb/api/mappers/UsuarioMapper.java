package sv.com.factuweb.api.mappers;

import org.springframework.stereotype.Component;
import sv.com.factuweb.api.dto.UsuarioDTO;
import sv.com.factuweb.api.entities.Usuario;

/** Convierte entre la entidad Usuario y su DTO. */
@Component
public class UsuarioMapper {

    /**
     * La contrasena nunca se devuelve: se envia siempre en null, aunque
     * la entidad la tenga cargada.
     */
    public UsuarioDTO aDTO(Usuario u, long facturasEmitidas) {
        if (u == null) return null;

        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(u.getIdUsuario());
        dto.setNombre(u.getNombre());
        dto.setEmail(u.getEmail());
        dto.setPassword(null);
        dto.setRol(u.getRol().name());
        dto.setActivo(u.getActivo());
        dto.setFacturasEmitidas(facturasEmitidas);
        return dto;
    }
}
