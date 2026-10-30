// src/utils/alertasApi.js

/**
 * Extrae un mensaje legible del error que devuelve la API.
 *
 * Si la respuesta no trae el formato propio del backend, se muestra el
 * codigo HTTP y lo que haya venido, para no dejar al usuario con un
 * mensaje generico que no dice nada.
 */
export const mensajeError = (error) => {
  if (!error.response) {
    return 'No se pudo conectar con el servidor. Verifique que la API esté corriendo en el puerto 8080.'
  }

  const { status, data } = error.response

  if (data?.campos) return Object.values(data.campos).join('. ')
  if (data?.mensaje) return data.mensaje

  /* Respuesta con otro formato: se arma un mensaje con lo disponible. */
  const detalle = data?.message || data?.error || (typeof data === 'string' ? data : '')

  if (status === 401) return 'Correo o contraseña incorrectos'
  if (status === 403) return 'No tiene permisos para realizar esta acción'
  if (status === 404) return detalle || 'No se encontró el recurso solicitado'
  if (status >= 500) {
    return `Error en el servidor (${status})${detalle ? ': ' + detalle : ''}. ` +
           'Revise la consola de IntelliJ para ver el detalle.'
  }
  return detalle || `La API respondió con el código ${status}`
}
