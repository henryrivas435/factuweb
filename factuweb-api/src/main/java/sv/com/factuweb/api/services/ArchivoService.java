package sv.com.factuweb.api.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import sv.com.factuweb.api.exception.ReglaNegocioException;
import sv.com.factuweb.api.interfaces.IArchivoService;

/**
 * Guarda las imagenes que suben los negocios: su logotipo y las fotos de
 * sus productos.
 *
 * Los archivos se escriben en una carpeta del servidor y en la base de datos
 * solo se guarda la ruta. Guardar la imagen dentro de la tabla haria crecer
 * la base innecesariamente y complicaria las consultas.
 *
 * Cada archivo se renombra con un identificador unico y se ubica en una
 * subcarpeta por empresa, para que un negocio no pueda sobrescribir el
 * logotipo de otro ni adivinar la ruta de sus imagenes.
 */
@Service
public class ArchivoService implements IArchivoService {

    private static final List<String> TIPOS_PERMITIDOS =
            List.of("image/jpeg", "image/png", "image/webp", "image/gif");
    private static final List<String> EXTENSIONES =
            List.of("jpg", "jpeg", "png", "webp", "gif");
    private static final long TAMANO_MAXIMO = 2L * 1024 * 1024;   // 2 MB

    private final Path raiz;
    private final String rutaPublica;

    public ArchivoService(@Value("${factuweb.archivos.directorio}") String directorio,
                           @Value("${factuweb.archivos.ruta-publica}") String rutaPublica) {
        this.raiz = Paths.get(directorio).toAbsolutePath().normalize();
        this.rutaPublica = rutaPublica;
        try {
            Files.createDirectories(this.raiz);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "No se pudo crear la carpeta de archivos: " + this.raiz, e);
        }
    }

    /**
     * Guarda la imagen y devuelve la ruta con la que el frontend podra
     * mostrarla, por ejemplo: /archivos/1/logo/a1b2c3.png
     */
    @Override
    public String guardarImagen(MultipartFile archivo, Long idEmpresa, String tipo) {
        validar(archivo);

        String extension = extensionDe(archivo.getOriginalFilename());
        String nombre = UUID.randomUUID().toString().replace("-", "") + "." + extension;

        Path carpeta = raiz.resolve(String.valueOf(idEmpresa)).resolve(tipo);
        try {
            Files.createDirectories(carpeta);
            Path destino = carpeta.resolve(nombre);

            /* Comprobacion de seguridad: el destino debe quedar dentro de la
               carpeta raiz, nunca fuera de ella. */
            if (!destino.normalize().startsWith(raiz)) {
                throw new ReglaNegocioException("Ruta de archivo no permitida");
            }

            Files.copy(archivo.getInputStream(), destino,
                       StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ReglaNegocioException(
                    "No se pudo guardar la imagen: " + e.getMessage());
        }

        return String.format("%s/%d/%s/%s", rutaPublica, idEmpresa, tipo, nombre);
    }

    /** Borra una imagen anterior cuando se reemplaza. Si no existe, no hace nada. */
    @Override
    public void borrarSiExiste(String rutaGuardada) {
        if (rutaGuardada == null || !rutaGuardada.startsWith(rutaPublica)) return;
        try {
            Path archivo = raiz.resolve(rutaGuardada.substring(rutaPublica.length() + 1))
                               .normalize();
            if (archivo.startsWith(raiz)) {
                Files.deleteIfExists(archivo);
            }
        } catch (IOException ignorada) {
            /* Que no se pueda borrar la imagen vieja no debe impedir
               guardar la nueva. */
        }
    }

    private void validar(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new ReglaNegocioException("No se recibió ninguna imagen");
        }
        if (archivo.getSize() > TAMANO_MAXIMO) {
            throw new ReglaNegocioException(
                    "La imagen no debe pesar más de 2 MB");
        }
        String tipo = archivo.getContentType();
        if (tipo == null || !TIPOS_PERMITIDOS.contains(tipo.toLowerCase(Locale.ROOT))) {
            throw new ReglaNegocioException(
                    "Formato no permitido. Use una imagen JPG, PNG, WEBP o GIF");
        }
    }

    private String extensionDe(String nombreOriginal) {
        if (nombreOriginal == null || !nombreOriginal.contains(".")) return "png";
        String ext = nombreOriginal
                .substring(nombreOriginal.lastIndexOf('.') + 1)
                .toLowerCase(Locale.ROOT);
        return EXTENSIONES.contains(ext) ? ext : "png";
    }
}
