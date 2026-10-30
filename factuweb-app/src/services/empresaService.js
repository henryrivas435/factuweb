// src/services/empresaService.js
import client from './axiosClient'

/* Datos del negocio del usuario que inicio sesion. */
export const empresaService = {
  obtener: () => client.get('/empresa'),
  actualizar: (datos) => client.put('/empresa', datos),
}
