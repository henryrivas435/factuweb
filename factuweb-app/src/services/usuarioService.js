// src/services/usuarioService.js
import client from './axiosClient'

/* Vendedores: cuentas de usuario del negocio. */
export const usuarioService = {
  listar: () => client.get('/usuarios'),
  crear: (datos) => client.post('/usuarios', datos),
  actualizar: (id, datos) => client.put(`/usuarios/${id}`, datos),
  desactivar: (id) => client.delete(`/usuarios/${id}`),
  reactivar: (id) => client.patch(`/usuarios/${id}/reactivar`),
}
