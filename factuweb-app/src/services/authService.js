// src/services/authService.js
import client from './axiosClient'

/* Autenticacion: login, registro de negocio y sesion actual. */
export const authService = {
  login: (email, password) => client.post('/auth/login', { email, password }),
  registrarNegocio: (datos) => client.post('/auth/registro', datos),
  sesionActual: () => client.get('/auth/yo'),
}
