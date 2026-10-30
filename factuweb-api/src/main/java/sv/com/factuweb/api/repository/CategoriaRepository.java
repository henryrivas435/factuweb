package sv.com.factuweb.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.com.factuweb.api.entities.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByEmpresaIdEmpresaOrderByNombreAsc(Long idEmpresa);

    Optional<Categoria> findByIdCategoriaAndEmpresaIdEmpresa(Long idCategoria, Long idEmpresa);
}
