package sv.com.factuweb.api.interfaces;

import sv.com.factuweb.api.dto.ProductoDTO;
import java.util.List;

/**
 * Inventario de un negocio. Es el otro CRUD de tabla independiente.
 *
 * La interfaz declara QUE hace el servicio; la implementacion en el
 * paquete services dice COMO lo hace. Los controladores dependen de
 * esta interfaz, no de la clase concreta.
 */
public interface IProductoService {

    List<ProductoDTO> list(Long idEmpresa, boolean incluirInactivos);

    List<ProductoDTO> find(Long idEmpresa, String texto);

    List<ProductoDTO> findBelowMinimum(Long idEmpresa);

    ProductoDTO get(Long idProducto, Long idEmpresa);

    ProductoDTO create(ProductoDTO peticion, Long idEmpresa);

    ProductoDTO update(Long idProducto, ProductoDTO peticion, Long idEmpresa);

    ProductoDTO deactivate(Long idProducto, Long idEmpresa);

    ProductoDTO reactivate(Long idProducto, Long idEmpresa);

}
