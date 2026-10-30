package sv.com.factuweb.api.mappers;

import org.springframework.stereotype.Component;
import sv.com.factuweb.api.dto.CategoriaDTO;
import sv.com.factuweb.api.entities.Categoria;

/** Convierte entre la entidad Categoria y su DTO. */
@Component
public class CategoriaMapper {

    public CategoriaDTO aDTO(Categoria c) {
        if (c == null) return null;

        CategoriaDTO dto = new CategoriaDTO();
        dto.setIdCategoria(c.getIdCategoria());
        dto.setNombre(c.getNombre());
        dto.setDescripcion(c.getDescripcion());
        return dto;
    }

    public void aEntidad(CategoriaDTO dto, Categoria c) {
        c.setNombre(dto.getNombre());
        c.setDescripcion(dto.getDescripcion());
    }
}
