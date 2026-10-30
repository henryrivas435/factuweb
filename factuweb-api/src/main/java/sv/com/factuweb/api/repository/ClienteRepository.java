package sv.com.factuweb.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.com.factuweb.api.entities.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByEmpresaIdEmpresaOrderByNombreAsc(Long idEmpresa);

    Optional<Cliente> findByIdClienteAndEmpresaIdEmpresa(Long idCliente, Long idEmpresa);

    /** Busqueda por nombre o documento dentro del negocio. */
    List<Cliente> findByEmpresaIdEmpresaAndNombreContainingIgnoreCaseOrEmpresaIdEmpresaAndDuiNitContainingIgnoreCase(
            Long idEmpresa1, String nombre, Long idEmpresa2, String duiNit);
}
