package sv.com.factuweb.api.services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.com.factuweb.api.dto.ProductoDTO;
import sv.com.factuweb.api.entities.Categoria;
import sv.com.factuweb.api.entities.Empresa;
import sv.com.factuweb.api.entities.Producto;
import sv.com.factuweb.api.exception.RecursoNoEncontradoException;
import sv.com.factuweb.api.exception.ReglaNegocioException;
import sv.com.factuweb.api.interfaces.IProductoService;
import sv.com.factuweb.api.mappers.ProductoMapper;
import sv.com.factuweb.api.repository.CategoriaRepository;
import sv.com.factuweb.api.repository.EmpresaRepository;
import sv.com.factuweb.api.repository.ProductoRepository;

/**
 * CRUD del stock.
 *
 * Los productos no se eliminan de la base de datos: se marcan como inactivos.
 * Asi las facturas antiguas siguen apuntando a una fila que existe y no
 * quedan registros huerfanos.
 */
@Service
public class ProductoService implements IProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;
    private final CategoriaRepository categoriaRepository;
    private final EmpresaRepository empresaRepository;

    public ProductoService(ProductoRepository productoRepository,
                            CategoriaRepository categoriaRepository,
                            EmpresaRepository empresaRepository,
                            ProductoMapper productoMapper) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.empresaRepository = empresaRepository;
        this.productoMapper = productoMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductoDTO> list(Long idEmpresa, boolean incluirInactivos) {
        List<Producto> productos = incluirInactivos
                ? productoRepository.findByEmpresaIdEmpresaOrderByNombreAsc(idEmpresa)
                : productoRepository.findByEmpresaIdEmpresaAndActivoTrueOrderByNombreAsc(idEmpresa);
        return productos.stream().map(productoMapper::aDTO).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductoDTO> find(Long idEmpresa, String texto) {
        return productoRepository.buscar(idEmpresa, texto).stream().map(productoMapper::aDTO).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductoDTO> findBelowMinimum(Long idEmpresa) {
        return productoRepository.bajoMinimo(idEmpresa).stream().map(productoMapper::aDTO).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public ProductoDTO get(Long idProducto, Long idEmpresa) {
        return productoMapper.aDTO(findById(idProducto, idEmpresa));
    }

    @Transactional
    @Override
    public ProductoDTO create(ProductoDTO peticion, Long idEmpresa) {
        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new RecursoNoEncontradoException("Negocio no encontrado"));

        if (peticion.getItem() != null && !peticion.getItem().isBlank()
                && productoRepository.existsByEmpresaIdEmpresaAndItem(idEmpresa, peticion.getItem())) {
            throw new ReglaNegocioException(
                    "Ya existe un producto con el item " + peticion.getItem());
        }

        Producto producto = new Producto();
        producto.setEmpresa(empresa);
        producto.setActivo(true);
        applyRequest(producto, peticion, idEmpresa);
        return productoMapper.aDTO(productoRepository.save(producto));
    }

    @Transactional
    @Override
    public ProductoDTO update(Long idProducto, ProductoDTO peticion, Long idEmpresa) {
        Producto producto = findById(idProducto, idEmpresa);
        applyRequest(producto, peticion, idEmpresa);
        return productoMapper.aDTO(productoRepository.save(producto));
    }

    /** Baja logica: el producto deja de aparecer en los listados. */
    @Transactional
    @Override
    public ProductoDTO deactivate(Long idProducto, Long idEmpresa) {
        Producto producto = findById(idProducto, idEmpresa);
        producto.setActivo(false);
        return productoMapper.aDTO(productoRepository.save(producto));
    }

    @Transactional
    @Override
    public ProductoDTO reactivate(Long idProducto, Long idEmpresa) {
        Producto producto = findById(idProducto, idEmpresa);
        producto.setActivo(true);
        return productoMapper.aDTO(productoRepository.save(producto));
    }

    private Producto findById(Long idProducto, Long idEmpresa) {
        return productoRepository.findByIdProductoAndEmpresaIdEmpresa(idProducto, idEmpresa)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro el producto con id " + idProducto));
    }

    private void applyRequest(Producto producto, ProductoDTO p, Long idEmpresa) {
        producto.setItem(p.getItem());
        producto.setNombre(p.getNombre());
        producto.setDescripcion(p.getDescripcion());
        producto.setPrecio(p.getPrecio());
        producto.setStock(p.getStock());
        producto.setStockMinimo(p.getStockMinimo() == null ? 0 : p.getStockMinimo());
        producto.setImagenUrl(p.getImagenUrl());

        if (p.getIdCategoria() != null) {
            Categoria categoria = categoriaRepository
                    .findByIdCategoriaAndEmpresaIdEmpresa(p.getIdCategoria(), idEmpresa)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "La categoria indicada no pertenece a este negocio"));
            producto.setCategoria(categoria);
        } else {
            producto.setCategoria(null);
        }
    }
}
