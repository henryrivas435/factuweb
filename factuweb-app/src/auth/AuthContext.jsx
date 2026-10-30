import { createContext, useContext, useState, useCallback } from 'react'
import { authService } from '../services/authService'
import { guardarSesion, leerSesion, borrarSesion } from '../services/axiosClient'

/**
 * Estado de la sesion compartido por toda la aplicacion.
 *
 * Guarda quien inicio sesion, con que rol y a que negocio pertenece.
 * Ese ultimo dato es el que hace que cada negocio vea solo lo suyo.
 */
const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [sesion, setSesion] = useState(() => leerSesion())

  const iniciarSesion = useCallback(async (email, password) => {
    const { data } = await authService.login(email, password)
    guardarSesion(data)
    setSesion(data)
    return data
  }, [])

  const registrarNegocio = useCallback(async (datos) => {
    const { data } = await authService.registrarNegocio(datos)
    guardarSesion(data)
    setSesion(data)
    return data
  }, [])

  const cerrarSesion = useCallback(() => {
    borrarSesion()
    setSesion(null)
  }, [])

  /**
   * Comprueba si el rol de la sesion esta dentro de los permitidos.
   * Sin lista de roles, basta con estar autenticado.
   */
  const tienePermiso = useCallback(
    (rolesPermitidos) => {
      if (!sesion) return false
      if (!rolesPermitidos || rolesPermitidos.length === 0) return true
      return rolesPermitidos.includes(sesion.rol)
    },
    [sesion]
  )

  const valor = {
    sesion,
    autenticado: Boolean(sesion),
    esAdministrador: sesion?.rol === 'ADMINISTRADOR',
    tienePermiso,
    iniciarSesion,
    registrarNegocio,
    cerrarSesion,
  }

  return <AuthContext.Provider value={valor}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const contexto = useContext(AuthContext)
  if (!contexto) {
    throw new Error('useAuth debe usarse dentro de AuthProvider')
  }
  return contexto
}
