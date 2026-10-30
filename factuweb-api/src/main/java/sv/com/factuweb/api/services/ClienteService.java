package sv.com.factuweb.api.services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.com.factuweb.api.dto.ClienteDTO;
import sv.com.factuweb.api.entities.Cliente;
import sv.com.factuweb.api.entities.Empresa;
import sv.com.factuweb.api.exception.RecursoNoEncontradoException;
import sv.com.factuweb.api.interfaces.IClienteService;
import sv.com.factuweb.api.mappers.ClienteMapper;
import sv.com.factuweb.api.repository.ClienteRepository;
import sv.com.factuweb.api.repository.EmpresaRepository;
import sv.com.factuweb.api.repository.FacturaRepository;

/**
 * CRUD de clientes. Todas las operaciones reciben el idEmpresa del usuario
 * autenticado, de modo que un negocio nunca acceda a los clientes de otro.
 */
@Service
public class ClienteService implements IClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final EmpresaRepository empresaRepository;
    private final FacturaRepository facturaRepository;

    public ClienteService(ClienteRepository clienteRepository,
                           EmpresaRepository empresaRepository,
                           FacturaRepository facturaRepository,
                           ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.empresaRepository = empresaRepository;
        this.facturaRepository = facturaRepository;
        this.clienteMapper = clienteMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ClienteDTO> listar(Long idEmpresa) {
        return clienteRepository.findByEmpresaIdEmpresaOrderByNombreAsc(idEmpresa)
                .stream().map(this::aDTOConConteo).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public ClienteDTO obtener(Long idCliente, Long idEmpresa) {
        return aDTOConConteo(buscar(idCliente, idEmpresa));
    }

    @Transactional
    @Override
    public ClienteDTO crear(ClienteDTO peticion, Long idEmpresa) {
        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new RecursoNoEncontradoException("Negocio no encontrado"));

        Cliente cliente = new Cliente();
        cliente.setEmpresa(empresa);
        aplicar(cliente, peticion);
        return aDTOConConteo(clienteRepository.save(cliente));
    }

    @Transactional
    @Override
    public ClienteDTO actualizar(Long idCliente, ClienteDTO peticion, Long idEmpresa) {
        Cliente cliente = buscar(idCliente, idEmpresa);
        aplicar(cliente, peticion);
        return aDTOConConteo(clienteRepository.save(cliente));
    }

    @Transactional
    @Override
    public void eliminar(Long idCliente, Long idEmpresa) {
        clienteRepository.delete(buscar(idCliente, idEmpresa));
    }

    private Cliente buscar(Long idCliente, Long idEmpresa) {
        return clienteRepository.findByIdClienteAndEmpresaIdEmpresa(idCliente, idEmpresa)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro el cliente con id " + idCliente));
    }

    private void aplicar(Cliente cliente, ClienteDTO p) {
        cliente.setNombre(p.getNombre());
        cliente.setDuiNit(p.getDuiNit());
        cliente.setDireccion(p.getDireccion());
        cliente.setTelefono(p.getTelefono());
    }

    /** Agrega el numero de facturas emitidas, que el mapper recibe aparte. */
    private ClienteDTO aDTOConConteo(Cliente c) {
        return clienteMapper.aDTO(c, facturaRepository.countByClienteIdCliente(c.getIdCliente()));
    }
}
