package sv.com.factuweb.api.mappers;

import org.springframework.stereotype.Component;
import sv.com.factuweb.api.dto.ClienteDTO;
import sv.com.factuweb.api.entities.Cliente;

/** Convierte entre la entidad Cliente y su DTO. */
@Component
public class ClienteMapper {

    public ClienteDTO aDTO(Cliente c, long facturasEmitidas) {
        if (c == null) return null;

        ClienteDTO dto = new ClienteDTO();
        dto.setIdCliente(c.getIdCliente());
        dto.setNombre(c.getNombre());
        dto.setDuiNit(c.getDuiNit());
        dto.setDireccion(c.getDireccion());
        dto.setTelefono(c.getTelefono());
        dto.setFacturasEmitidas(facturasEmitidas);
        return dto;
    }

    public void aEntidad(ClienteDTO dto, Cliente c) {
        c.setNombre(dto.getNombre());
        c.setDuiNit(dto.getDuiNit());
        c.setDireccion(dto.getDireccion());
        c.setTelefono(dto.getTelefono());
    }
}
