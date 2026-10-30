package sv.com.factuweb.api.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Calculos de facturacion.
 *
 * Todos los montos se manejan con BigDecimal y se redondean a dos
 * decimales. Usar double produciria centavos que no cuadran.
 */
public final class FacturaUtils {

    /** Cantidad de digitos del numero correlativo: 1 se convierte en 000001. */
    private static final String FORMATO_CORRELATIVO = "%06d";

    private FacturaUtils() {
    }

    /** Arma el siguiente numero correlativo a partir de las facturas ya emitidas. */
    public static String correlativo(long facturasPrevias) {
        return String.format(FORMATO_CORRELATIVO, facturasPrevias + 1);
    }

    /** Redondea un monto a dos decimales, redondeando hacia arriba desde 0.5. */
    public static BigDecimal redondear(BigDecimal monto) {
        return monto.setScale(2, RoundingMode.HALF_UP);
    }

    /** precio multiplicado por cantidad, ya redondeado. */
    public static BigDecimal subtotalLinea(BigDecimal precio, int cantidad) {
        return redondear(precio.multiply(BigDecimal.valueOf(cantidad)));
    }

    /** El impuesto que corresponde a un subtotal. */
    public static BigDecimal calcularIva(BigDecimal subtotal, BigDecimal porcentaje) {
        return redondear(subtotal.multiply(porcentaje));
    }
}
