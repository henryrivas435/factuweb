package sv.com.factuweb.api.enums;

/**
 * Estados posibles de una factura.
 *
 * Una factura nunca se elimina fisicamente de la base de datos: solo cambia
 * de estado. Esto conserva la numeracion correlativa y permite justificar
 * cualquier salto ante una revision.
 */
public enum EstadoFactura {
    EMITIDA,
    ANULADA,
    ELIMINADA
}
