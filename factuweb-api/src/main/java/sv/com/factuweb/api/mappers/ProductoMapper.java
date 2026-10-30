package sv.com.factuweb.api.mappers;

import org.springframework.stereotype.Component;
import sv.com.factuweb.api.dto.ProductoDTO;
import sv.com.factuweb.api.entities.Producto;

/**
 * Convierte entre la entidad Producto y su DTO.
 *
 * Tener la conversion aparte evita que los servicios se llenen de codigo
 * repetitivo de asignaciones, y deja un unico lugar que cambiar cuando se
 * agrega o quita un campo.
 */
@Component
public class ProductoMapper {

    /** De entidad a DTO: lo que se devuelve al frontend. */
    public ProductoDTO aDTO(Producto p) {
        if (p == null) return null;

        ProductoDTO dto = new ProductoDTO();
        dto.setIdProducto(p.getIdProducto());
        dto.setItem(p.getItem());
        dto.setNombre(p.getNombre());
        dto.setDescripcion(p.getDescripcion());
        dto.setPrecio(p.getPrecio());
        dto.setStock(p.getStock());
        dto.setStockMinimo(p.getStockMinimo());
        dto.setImagenUrl(p.getImagenUrl());
        dto.setActivo(p.getActivo());
        dto.setBajoMinimo(p.isBajoMinimo());

        if (p.getCategoria() != null) {
            dto.setIdCategoria(p.getCategoria().getIdCategoria());
            dto.setNombreCategoria(p.getCategoria().getNombre());
        }
        return dto;
    }

    /**
     * Del DTO a la entidad: copia solo los campos que el usuario puede
     * modificar. La empresa, la categoria y el estado los asigna el
     * servicio, porque dependen de reglas del negocio.
     */
    public void aEntidad(ProductoDTO dto, Producto p) {
        p.setItem(dto.getItem());
        p.setNombre(dto.getNombre());
        p.setDescripcion(dto.getDescripcion());
        p.setPrecio(dto.getPrecio());
        p.setStock(dto.getStock());
        p.setStockMinimo(dto.getStockMinimo() == null ? 0 : dto.getStockMinimo());
        p.setImagenUrl(dto.getImagenUrl());
    }
}
