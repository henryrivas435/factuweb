// src/services/reporteService.js
import client from './axiosClient'

/* Panel de indicadores y reportes. */
export const reporteService = {
  panel: () => client.get('/panel'),
  generar: (tipo, desde, hasta) =>
    client.get('/reportes', { params: { tipo, desde, hasta } }),
  historial: () => client.get('/reportes/historial'),
}
