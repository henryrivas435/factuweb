package sv.com.factuweb.api.exception;

/** Se lanza cuando la operacion viola una regla del negocio (stock insuficiente, correo repetido, etc.). */
public class ReglaNegocioException extends RuntimeException {
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
