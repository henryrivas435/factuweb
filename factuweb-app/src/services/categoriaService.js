// src/services/categoriaService.js
import client from './axiosClient'

/* Categorias de productos. */
export const categoriaService = {
  listar: () => client.get('/categorias'),
  crear: (datos) => client.post('/categorias', datos),
  actualizar: (id, datos) => client.put(`/categorias/${id}`, datos),
  eliminar: (id) => client.delete(`/categorias/${id}`),
}
