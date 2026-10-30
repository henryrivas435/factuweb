import axios from 'axios'
import { CLAVE_TOKEN, CLAVE_SESION } from '../utils/constants'

/**
 * Cliente HTTP centralizado.
 *
 * Todas las llamadas a la API pasan por aqui, de modo que el token se
 * adjunta en un solo lugar y no hay que repetirlo en cada componente.
 */
const client = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' },
})


export const guardarSesion = (datos) => {
  localStorage.setItem(CLAVE_TOKEN, datos.token)
  localStorage.setItem(CLAVE_SESION, JSON.stringify(datos))
}

export const leerSesion = () => {
  const crudo = localStorage.getItem(CLAVE_SESION)
  return crudo ? JSON.parse(crudo) : null
}

export const leerToken = () => localStorage.getItem(CLAVE_TOKEN)

export const borrarSesion = () => {
  localStorage.removeItem(CLAVE_TOKEN)
  localStorage.removeItem(CLAVE_SESION)
}

/* Antes de cada peticion se adjunta el token, si existe. */
client.interceptors.request.use((config) => {
  const token = leerToken()
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

/* Rutas que no necesitan sesion: si fallan, nunca deben mandar al login. */
const RUTAS_PUBLICAS = ['/auth/', '/publico/', '/facturas/verificar/']

/* Pantallas publicas: aunque el token este vencido, aqui no se redirige. */
const PANTALLAS_PUBLICAS = ['/', '/login', '/registro', '/verificar']

const esPantallaPublica = () => {
  const ruta = window.location.pathname
  return PANTALLAS_PUBLICAS.some((p) =>
    p === '/' ? ruta === '/' : ruta.startsWith(p)
  )
}

/*
 * Si la API responde 401, el token expiro o es invalido: se cierra la
 * sesion y se manda al login. El 403 (sin permisos) se deja pasar para
 * que cada pantalla muestre su propio mensaje.
 *
 * No se redirige cuando la peticion era a una ruta publica ni cuando el
 * usuario ya esta en una pantalla publica: en la pagina de inicio, por
 * ejemplo, un token vencido no debe sacar a nadie al login.
 */
client.interceptors.response.use(
  (respuesta) => respuesta,
  (error) => {
    const url = error.config?.url ?? ''
    const eraPublica = RUTAS_PUBLICAS.some((r) => url.includes(r))

    if (error.response?.status === 401 && !eraPublica && !esPantallaPublica()) {
      borrarSesion()
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default client
