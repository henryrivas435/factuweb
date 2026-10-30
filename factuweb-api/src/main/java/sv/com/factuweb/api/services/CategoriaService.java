package sv.com.factuweb.api.services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.com.factuweb.api.dto.CategoriaDTO;
import sv.com.factuweb.api.entities.Categoria;
import sv.com.factuweb.api.entities.Empresa;
import sv.com.factuweb.api.exception.RecursoNoEncontradoException;
import sv.com.factuweb.api.exception.ReglaNegocioException;
import sv.com.factuweb.api.interfaces.ICategoriaService;
import sv.com.factuweb.api.mappers.CategoriaMapper;
import sv.com.factuweb.api.repository.CategoriaRepository;
import sv.com.factuweb.api.repository.EmpresaRepository;
import sv.com.factuweb.api.repository.ProductoRepository;

@Service
public class CategoriaService implements ICategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;
    private final ProductoRepository productoRepository;
    private final EmpresaRepository empresaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository,
                             EmpresaRepository empresaRepository,
                             CategoriaMapper categoriaMapper,
                             ProductoRepository productoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.empresaRepository = empresaRepository;
        this.categoriaMapper = categoriaMapper;
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoriaDTO> list(Long idEmpresa) {
        return categoriaRepository.findByEmpresaIdEmpresaOrderByNombreAsc(idEmpresa)
                .stream().map(categoriaMapper::aDTO).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public CategoriaDTO get(Long idCategoria, Long idEmpresa) {
        return categoriaMapper.aDTO(findById(idCategoria, idEmpresa));
    }

    @Transactional
    @Override
    public CategoriaDTO create(CategoriaDTO peticion, Long idEmpresa) {
        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new RecursoNoEncontradoException("Negocio no encontrado"));

        Categoria categoria = new Categoria();
        categoria.setEmpresa(empresa);
        categoria.setNombre(peticion.getNombre());
        categoria.setDescripcion(peticion.getDescripcion());
        return categoriaMapper.aDTO(categoriaRepository.save(categoria));
    }

    @Transactional
    @Override
    public CategoriaDTO update(Long idCategoria, CategoriaDTO peticion, Long idEmpresa) {
        Categoria categoria = findById(idCategoria, idEmpresa);
        categoria.setNombre(peticion.getNombre());
        categoria.setDescripcion(peticion.getDescripcion());
        return categoriaMapper.aDTO(categoriaRepository.save(categoria));
    }

    @Transactional
    @Override
    public void delete(Long idCategoria, Long idEmpresa) {
        Categoria categoria = findById(idCategoria, idEmpresa);

        // Se valida ANTES de borrar para devolver un mensaje entendible, en
        // vez de dejar que reviente la llave foranea de PostgreSQL con un
        // error 500 que no le dice nada al usuario.
        if (productoRepository.existsByCategoriaIdCategoria(idCategoria)) {
            throw new ReglaNegocioException(
                    "No se puede eliminar la categoria porque tiene productos asignados");
        }

        categoriaRepository.delete(categoria);
    }

    private Categoria findById(Long idCategoria, Long idEmpresa) {
        return categoriaRepository.findByIdCategoriaAndEmpresaIdEmpresa(idCategoria, idEmpresa)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro la categoria con id " + idCategoria));
    }
}
