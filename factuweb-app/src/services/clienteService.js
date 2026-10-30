// src/services/clienteService.js
import client from './axiosClient'

/* Clientes del negocio. */
export const clienteService = {
  listar: () => client.get('/clientes'),
  obtener: (id) => client.get(`/clientes/${id}`),
  crear: (datos) => client.post('/clientes', datos),
  actualizar: (id, datos) => client.put(`/clientes/${id}`, datos),
  eliminar: (id) => client.delete(`/clientes/${id}`),
}
