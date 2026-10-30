package sv.com.factuweb.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.com.factuweb.api.entities.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    /** Todos los usuarios de un negocio. */
    List<Usuario> findByEmpresaIdEmpresaOrderByNombreAsc(Long idEmpresa);

    /** Un usuario concreto, verificando que pertenezca al negocio indicado. */
    Optional<Usuario> findByIdUsuarioAndEmpresaIdEmpresa(Long idUsuario, Long idEmpresa);

    long countByEmpresaIdEmpresaAndActivoTrue(Long idEmpresa);
}
