package sv.com.factuweb.api.interfaces;

import sv.com.factuweb.api.dto.CategoriaDTO;
import java.util.List;

/**
 * Categorias de productos definidas por cada negocio.
 *
 * La interfaz declara QUE hace el servicio; la implementacion en el
 * paquete services dice COMO lo hace. Los controladores dependen de
 * esta interfaz, no de la clase concreta.
 */
public interface ICategoriaService {

    List<CategoriaDTO> list(Long idEmpresa);

    CategoriaDTO get(Long idCategoria, Long idEmpresa);

    CategoriaDTO create(CategoriaDTO peticion, Long idEmpresa);

    CategoriaDTO update(Long idCategoria, CategoriaDTO peticion, Long idEmpresa);

    void delete(Long idCategoria, Long idEmpresa);

}
