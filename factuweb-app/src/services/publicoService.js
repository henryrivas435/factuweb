// src/services/publicoService.js
import client from './axiosClient'

/* Parte publica del sitio: no requiere sesion. */
export const publicoService = {
  negocios: () => client.get('/publico/negocios'),
  verificarFactura: (numero) => client.get(`/facturas/verificar/${numero}`),
}
