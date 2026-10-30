package sv.com.factuweb.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sv.com.factuweb.api.security.UsuarioPrincipal;
import sv.com.factuweb.api.interfaces.IArchivoService;

import java.util.Map;

/**
 * Carga de imagenes: el logotipo del negocio y las fotos de los productos.
 *
 * Devuelve la ruta con la que quedo guardada la imagen; esa ruta es la que
 * despues se guarda en el campo logo_url o imagen_url.
 */
@RestController
@RequestMapping("/api/archivos")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class ArchivoController {

    private final IArchivoService archivoService;

    public ArchivoController(IArchivoService archivoService) {
        this.archivoService = archivoService;
    }

    @PostMapping("/logo")
    public ResponseEntity<Map<String, String>> subirLogo(
            @RequestParam("archivo") MultipartFile archivo,
            @AuthenticationPrincipal UsuarioPrincipal u) {
        String ruta = archivoService.guardarImagen(archivo, u.getIdEmpresa(), "logo");
        return ResponseEntity.ok(Map.of("url", ruta));
    }

    @PostMapping("/producto")
    public ResponseEntity<Map<String, String>> subirImagenProducto(
            @RequestParam("archivo") MultipartFile archivo,
            @AuthenticationPrincipal UsuarioPrincipal u) {
        String ruta = archivoService.guardarImagen(archivo, u.getIdEmpresa(), "productos");
        return ResponseEntity.ok(Map.of("url", ruta));
    }
}
