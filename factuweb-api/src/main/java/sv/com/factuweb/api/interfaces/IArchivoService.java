package sv.com.factuweb.api.interfaces;

import org.springframework.web.multipart.MultipartFile;

/**
 * Guarda las imagenes que suben los negocios: logotipos y fotos de productos.
 *
 * La interfaz declara QUE hace el servicio; la implementacion en el
 * paquete services dice COMO lo hace. Los controladores dependen de
 * esta interfaz, no de la clase concreta.
 */
public interface IArchivoService {

    String guardarImagen(MultipartFile archivo, Long idEmpresa, String tipo);

    void borrarSiExiste(String rutaGuardada);

}