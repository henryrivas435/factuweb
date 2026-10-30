package sv.com.factuweb.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/** Convierte las excepciones en respuestas JSON con un formato uniforme. */
@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    private Map<String, Object> cuerpo(HttpStatus estado, String mensaje) {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("momento", LocalDateTime.now());
        r.put("estado", estado.value());
        r.put("error", estado.getReasonPhrase());
        r.put("mensaje", mensaje);
        return r;
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> noEncontrado(RecursoNoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(cuerpo(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<Map<String, Object>> reglaNegocio(ReglaNegocioException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(cuerpo(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> credenciales(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(cuerpo(HttpStatus.UNAUTHORIZED, "Correo o contrasena incorrectos"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> accesoDenegado(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(cuerpo(HttpStatus.FORBIDDEN, "No tiene permisos para realizar esta accion"));
    }

    /** Errores de validacion de los DTO: devuelve el detalle campo por campo. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validacion(MethodArgumentNotValidException e) {
        Map<String, String> errores = new HashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));

        Map<String, Object> r = cuerpo(HttpStatus.BAD_REQUEST, "Hay campos con errores");
        r.put("campos", errores);
        return ResponseEntity.badRequest().body(r);
    }

    /**
     * La imagen supera el limite configurado en application.yml. Spring corta
     * la peticion antes de llegar al controlador, asi que sin este manejador
     * el usuario veria un error 500 sin explicacion.
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, Object>> archivoMuyGrande(MaxUploadSizeExceededException e) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(cuerpo(HttpStatus.PAYLOAD_TOO_LARGE,
                        "La imagen es demasiado pesada. El máximo permitido es 2 MB."));
    }

    /** El cuerpo de la peticion llego mal formado o con un tipo inesperado. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> cuerpoInvalido(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest()
                .body(cuerpo(HttpStatus.BAD_REQUEST,
                        "Los datos enviados no tienen el formato esperado"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> general(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(cuerpo(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Ocurrio un error inesperado: " + e.getMessage()));
    }
}
