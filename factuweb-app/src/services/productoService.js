// src/services/productoService.js
import client from './axiosClient'

/* Stock: productos del negocio. */
export const productoService = {
  listar: (incluirInactivos = false) =>
    client.get('/productos', { params: { incluirInactivos } }),
  buscar: (texto) => client.get('/productos/buscar', { params: { texto } }),
  bajoMinimo: () => client.get('/productos/bajo-minimo'),
  obtener: (id) => client.get(`/productos/${id}`),
  crear: (datos) => client.post('/productos', datos),
  actualizar: (id, datos) => client.put(`/productos/${id}`, datos),
  desactivar: (id) => client.delete(`/productos/${id}`),
  reactivar: (id) => client.patch(`/productos/${id}/reactivar`),
}
