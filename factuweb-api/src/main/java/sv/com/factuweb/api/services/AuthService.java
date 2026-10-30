package sv.com.factuweb.api.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.com.factuweb.api.dto.auth.LoginRequestDTO;
import sv.com.factuweb.api.dto.auth.LoginResponseDTO;
import sv.com.factuweb.api.dto.auth.RegistroNegocioDTO;
import sv.com.factuweb.api.entities.Empresa;
import sv.com.factuweb.api.entities.Usuario;
import sv.com.factuweb.api.enums.Rol;
import sv.com.factuweb.api.exception.ReglaNegocioException;
import sv.com.factuweb.api.interfaces.IAuthService;
import sv.com.factuweb.api.repository.EmpresaRepository;
import sv.com.factuweb.api.repository.UsuarioRepository;
import sv.com.factuweb.api.security.JwtService;
import sv.com.factuweb.api.security.UsuarioPrincipal;

@Service
public class AuthService implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager,
                        JwtService jwtService,
                        EmpresaRepository empresaRepository,
                        UsuarioRepository usuarioRepository,
                        PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.empresaRepository = empresaRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /** Valida las credenciales y devuelve el token con los datos de la sesion. */
    @Override
    public LoginResponseDTO iniciarSesion(LoginRequestDTO peticion) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(peticion.getEmail(), peticion.getPassword()));

        UsuarioPrincipal usuario = (UsuarioPrincipal) auth.getPrincipal();
        String token = jwtService.generarToken(usuario);

        Empresa empresa = empresaRepository.findById(usuario.getIdEmpresa()).orElseThrow();

        return new LoginResponseDTO(
                token,
                jwtService.getExpiracionMs(),
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol().name(),
                empresa.getIdEmpresa(),
                empresa.getNombre());
    }

    /**
     * Registra un negocio nuevo junto con su usuario administrador.
     *
     * Las dos inserciones ocurren en una sola transaccion: si falla la
     * creacion del usuario, no debe quedar una empresa sin administrador.
     */
    @Transactional
    @Override
    public LoginResponseDTO registrarNegocio(RegistroNegocioDTO peticion) {

        if (usuarioRepository.existsByEmail(peticion.getEmailAdmin())) {
            throw new ReglaNegocioException(
                    "Ya existe una cuenta registrada con el correo " + peticion.getEmailAdmin());
        }
        if (peticion.getNit() != null && !peticion.getNit().isBlank()
                && empresaRepository.existsByNit(peticion.getNit())) {
            throw new ReglaNegocioException(
                    "Ya hay un negocio registrado con el NIT " + peticion.getNit());
        }

        Empresa empresa = new Empresa();
        empresa.setNombre(peticion.getNombreNegocio());
        empresa.setRubro(peticion.getRubro());
        empresa.setNit(peticion.getNit());
        empresa.setDireccion(peticion.getDireccion());
        empresa.setTelefono(peticion.getTelefono());
        empresa.setLogoUrl(peticion.getLogoUrl());
        empresa.setActivo(true);
        empresa = empresaRepository.save(empresa);

        Usuario admin = new Usuario();
        admin.setEmpresa(empresa);
        admin.setNombre(peticion.getNombreAdmin());
        admin.setEmail(peticion.getEmailAdmin());
        admin.setPassword(passwordEncoder.encode(peticion.getPasswordAdmin()));
        admin.setRol(Rol.ADMINISTRADOR);
        admin.setActivo(true);
        usuarioRepository.save(admin);

        UsuarioPrincipal principal = new UsuarioPrincipal(admin);
        String token = jwtService.generarToken(principal);

        return new LoginResponseDTO(
                token,
                jwtService.getExpiracionMs(),
                admin.getIdUsuario(),
                admin.getNombre(),
                admin.getEmail(),
                admin.getRol().name(),
                empresa.getIdEmpresa(),
                empresa.getNombre());
    }
}
