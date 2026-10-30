package sv.com.factuweb.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * FactuWeb es una plataforma de facturacion multiempresa: varios negocios
 * independientes conviven en la misma base de datos, y cada uno solo puede
 * ver y modificar su propia informacion. Esa separacion se logra con la
 * llave foranea id_empresa presente en casi todas las tablas.
 */
@SpringBootApplication
public class FactuwebApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FactuwebApiApplication.class, args);
    }
}
