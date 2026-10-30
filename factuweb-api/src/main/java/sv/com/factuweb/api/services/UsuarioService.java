package sv.com.factuweb.api.services;

import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.com.factuweb.api.dto.UsuarioDTO;
import sv.com.factuweb.api.entities.Empresa;
import sv.com.factuweb.api.entities.Usuario;
import sv.com.factuweb.api.enums.Rol;
import sv.com.factuweb.api.exception.RecursoNoEncontradoException;
import sv.com.factuweb.api.exception.ReglaNegocioException;
import sv.com.factuweb.api.interfaces.IUsuarioService;
import sv.com.factuweb.api.mappers.UsuarioMapper;
import sv.com.factuweb.api.repository.EmpresaRepository;
import sv.com.factuweb.api.repository.FacturaRepository;
import sv.com.factuweb.api.repository.UsuarioRepository;

/**
 * Gestion de los vendedores de un negocio.
 * Solo el administrador puede usar estas operaciones, y siempre dentro de su
 * propio negocio: no puede crear ni modificar usuarios de otra empresa.
 */
@Service
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final EmpresaRepository empresaRepository;
    private final FacturaRepository facturaRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                           EmpresaRepository empresaRepository,
                           FacturaRepository facturaRepository,
                           PasswordEncoder passwordEncoder,
                           UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.empresaRepository = empresaRepository;
        this.facturaRepository = facturaRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public List<UsuarioDTO> listar(Long idEmpresa) {
        return usuarioRepository.findByEmpresaIdEmpresaOrderByNombreAsc(idEmpresa)
                .stream().map(this::aDTOConConteo).toList();
    }

    @Transactional
    @Override
    public UsuarioDTO crear(UsuarioDTO peticion, Long idEmpresa) {
        if (peticion.getPassword() == null || peticion.getPassword().isBlank()) {
            throw new ReglaNegocioException(
                    "La contrasena es obligatoria al crear una cuenta");
        }
        if (usuarioRepository.existsByEmail(peticion.getEmail())) {
            throw new ReglaNegocioException(
                    "Ya existe una cuenta con el correo " + peticion.getEmail());
        }

        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new RecursoNoEncontradoException("Negocio no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setEmpresa(empresa);
        usuario.setNombre(peticion.getNombre());
        usuario.setEmail(peticion.getEmail());
        usuario.setPassword(passwordEncoder.encode(peticion.getPassword()));
        usuario.setRol(Rol.valueOf(peticion.getRol()));
        usuario.setActivo(true);

        return aDTOConConteo(usuarioRepository.save(usuario));
    }

    /** Al actualizar, la contrasena es opcional: si viene vacia no se cambia. */
    @Transactional
    @Override
    public UsuarioDTO actualizar(Long idUsuario, UsuarioDTO peticion, Long idEmpresa) {
        Usuario usuario = buscar(idUsuario, idEmpresa);

        if (!usuario.getEmail().equals(peticion.getEmail())
                && usuarioRepository.existsByEmail(peticion.getEmail())) {
            throw new ReglaNegocioException(
                    "Ya existe una cuenta con el correo " + peticion.getEmail());
        }

        usuario.setNombre(peticion.getNombre());
        usuario.setEmail(peticion.getEmail());
        usuario.setRol(Rol.valueOf(peticion.getRol()));
        if (peticion.getPassword() != null && !peticion.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(peticion.getPassword()));
        }

        return aDTOConConteo(usuarioRepository.save(usuario));
    }

    /** Baja logica de la cuenta. No se elimina para conservar sus facturas. */
    @Transactional
    @Override
    public void desactivar(Long idUsuario, Long idEmpresa, Long idUsuarioActual) {
        if (idUsuario.equals(idUsuarioActual)) {
            throw new ReglaNegocioException("No puede desactivar su propia cuenta");
        }
        Usuario usuario = buscar(idUsuario, idEmpresa);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    @Transactional
    @Override
    public void reactivar(Long idUsuario, Long idEmpresa) {
        Usuario usuario = buscar(idUsuario, idEmpresa);
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
    }

    private Usuario buscar(Long idUsuario, Long idEmpresa) {
        return usuarioRepository.findByIdUsuarioAndEmpresaIdEmpresa(idUsuario, idEmpresa)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro el usuario con id " + idUsuario));
    }

    /** Agrega el numero de facturas emitidas, que el mapper recibe aparte. */
    private UsuarioDTO aDTOConConteo(Usuario u) {
        return usuarioMapper.aDTO(u, facturaRepository.countByUsuarioIdUsuario(u.getIdUsuario()));
    }
}
