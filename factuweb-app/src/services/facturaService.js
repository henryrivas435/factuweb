// src/services/facturaService.js
import client from './axiosClient'

/* Facturas emitidas. */
export const facturaService = {
  listar: () => client.get('/facturas'),
  porPeriodo: (desde, hasta, estado) =>
    client.get('/facturas/periodo', { params: { desde, hasta, estado } }),
  obtener: (id) => client.get(`/facturas/${id}`),
  emitir: (datos) => client.post('/facturas', datos),
  anular: (id) => client.patch(`/facturas/${id}/anular`),
}
