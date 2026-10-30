package sv.com.factuweb.api.utils;

/** Utilidades de texto compartidas por varias capas. */
public final class TextoUtils {

    private TextoUtils() {
        // Clase de utilidades: no se instancia.
    }

    /**
     * Iniciales de un negocio, para mostrarlas cuando no ha subido logotipo.
     * Toma la primera letra de las dos primeras palabras significativas:
     * "Libreria y Papeleria El Progreso" da "LP".
     */
    public static String iniciales(String nombre) {
        if (nombre == null || nombre.isBlank()) return "??";

        StringBuilder sb = new StringBuilder();
        for (String parte : nombre.trim().split("\\s+")) {
            if (parte.length() > 2 && sb.length() < 2) {
                sb.append(Character.toUpperCase(parte.charAt(0)));
            }
        }
        if (sb.length() == 0) {
            sb.append(Character.toUpperCase(nombre.charAt(0)));
        }
        return sb.toString();
    }
}
