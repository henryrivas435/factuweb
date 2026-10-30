package sv.com.factuweb.api.exception;

/** Se lanza cuando el registro no existe o pertenece a otro negocio. */
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}