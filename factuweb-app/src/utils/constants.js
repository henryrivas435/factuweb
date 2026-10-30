// src/utils/constants.js

/** Claves con las que se guarda la sesion en localStorage*/
export const CLAVE_TOKEN = "factuweb_token";
export const CLAVE_SESION = "factuweb_sesion";

/** Roles que maneja el backend (enum Rol)*/
export const ROLES = {
  ADMINISTRADOR: "ADMINISTRADOR",
  VENDEDOR: "VENDEDOR",
};

/**
 * Las imagenes las sirve el backend fuera del prefijo /api, en /archivos.
 * En desarrollo el proxy de Vite redirige esa ruta al puerto 8080, asi que
 * basta con la ruta que devuelve el backend
 */
export const construirUrlArchivo = (ruta) => {
  if (!ruta) return null;
  return ruta.startsWith("http") ? ruta : ruta;
};
