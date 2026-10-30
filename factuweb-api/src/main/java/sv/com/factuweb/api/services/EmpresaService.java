package sv.com.factuweb.api.services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.com.factuweb.api.dto.EmpresaDTO;
import sv.com.factuweb.api.dto.EmpresaPublicaDTO;
import sv.com.factuweb.api.entities.Empresa;
import sv.com.factuweb.api.exception.RecursoNoEncontradoException;
import sv.com.factuweb.api.interfaces.IEmpresaService;
import sv.com.factuweb.api.mappers.EmpresaMapper;
import sv.com.factuweb.api.repository.EmpresaRepository;

@Service
public class EmpresaService implements IEmpresaService {

    private final EmpresaRepository empresaRepository;
    private final EmpresaMapper empresaMapper;

    public EmpresaService(EmpresaRepository empresaRepository,
                           EmpresaMapper empresaMapper) {
        this.empresaRepository = empresaRepository;
        this.empresaMapper = empresaMapper;
    }

    /** Negocios que se muestran en la pagina publica de la plataforma. */
    @Transactional(readOnly = true)
    @Override
    public List<EmpresaPublicaDTO> listarPublicas() {
        return empresaRepository.findByActivoTrueOrderByNombreAsc().stream()
                .map(e -> new EmpresaPublicaDTO(
                        e.getIdEmpresa(),
                        e.getNombre(),
                        e.getRubro(),
                        iniciales(e.getNombre()),
                        e.getLogoUrl(),
                        empresaRepository.contarProductos(e.getIdEmpresa()),
                        empresaRepository.contarFacturas(e.getIdEmpresa())))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public EmpresaDTO obtener(Long idEmpresa) {
        return empresaMapper.aDTO(buscar(idEmpresa));
    }

    @Transactional
    @Override
    public EmpresaDTO actualizar(Long idEmpresa, EmpresaDTO datos) {
        Empresa empresa = buscar(idEmpresa);
        empresa.setNombre(datos.getNombre());
        empresa.setRubro(datos.getRubro());
        empresa.setNit(datos.getNit());
        empresa.setDireccion(datos.getDireccion());
        empresa.setTelefono(datos.getTelefono());
        empresa.setLogoUrl(datos.getLogoUrl());
        return empresaMapper.aDTO(empresaRepository.save(empresa));
    }

    private Empresa buscar(Long idEmpresa) {
        return empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el negocio con id " + idEmpresa));
    }



    /** Iniciales del negocio, usadas como distintivo en la pagina publica. */
    private String iniciales(String nombre) {
        if (nombre == null || nombre.isBlank()) return "??";
        StringBuilder sb = new StringBuilder();
        for (String parte : nombre.trim().split("\\s+")) {
            if (parte.length() > 2 && sb.length() < 2) {
                sb.append(Character.toUpperCase(parte.charAt(0)));
            }
        }
        if (sb.length() == 0) sb.append(Character.toUpperCase(nombre.charAt(0)));
        return sb.toString();
    }
}
