package sv.com.factuweb.api.mappers;

import org.springframework.stereotype.Component;
import sv.com.factuweb.api.dto.EmpresaDTO;
import sv.com.factuweb.api.dto.EmpresaPublicaDTO;
import sv.com.factuweb.api.entities.Empresa;
import sv.com.factuweb.api.utils.TextoUtils;

/** Convierte entre la entidad Empresa y sus DTO. */
@Component
public class EmpresaMapper {

    /** Datos completos: solo para el propio negocio. */
    public EmpresaDTO aDTO(Empresa e) {
        if (e == null) return null;

        EmpresaDTO dto = new EmpresaDTO();
        dto.setIdEmpresa(e.getIdEmpresa());
        dto.setNombre(e.getNombre());
        dto.setRubro(e.getRubro());
        dto.setNit(e.getNit());
        dto.setDireccion(e.getDireccion());
        dto.setTelefono(e.getTelefono());
        dto.setLogoUrl(e.getLogoUrl());
        dto.setFechaRegistro(e.getFechaRegistro());
        dto.setActivo(e.getActivo());
        return dto;
    }

    /** Version reducida para la pagina publica: sin NIT ni direccion. */
    public EmpresaPublicaDTO aPublicaDTO(Empresa e, long totalProductos, long totalFacturas) {
        if (e == null) return null;

        return new EmpresaPublicaDTO(
                e.getIdEmpresa(),
                e.getNombre(),
                e.getRubro(),
                TextoUtils.iniciales(e.getNombre()),
                e.getLogoUrl(),
                totalProductos,
                totalFacturas);
    }

    public void aEntidad(EmpresaDTO dto, Empresa e) {
        e.setNombre(dto.getNombre());
        e.setRubro(dto.getRubro());
        e.setNit(dto.getNit());
        e.setDireccion(dto.getDireccion());
        e.setTelefono(dto.getTelefono());
        e.setLogoUrl(dto.getLogoUrl());
    }
}
