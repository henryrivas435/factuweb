package sv.com.factuweb.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * Publica la carpeta de imagenes para que el navegador pueda mostrarlas.
 *
 * Una peticion a /archivos/1/logo/foto.png entrega el archivo que esta en
 * la carpeta configurada en application.yml.
 */
@Configuration
public class ArchivosConfig implements WebMvcConfigurer {

    private final String directorio;
    private final String rutaPublica;

    public ArchivosConfig(
            @Value("${factuweb.archivos.directorio}") String directorio,
            @Value("${factuweb.archivos.ruta-publica}") String rutaPublica) {
        this.directorio = directorio;
        this.rutaPublica = rutaPublica;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registro) {
        String carpeta = Paths.get(directorio).toAbsolutePath().normalize().toUri().toString();
        registro.addResourceHandler(rutaPublica + "/**")
                .addResourceLocations(carpeta)
                .setCachePeriod(3600);
    }
}
