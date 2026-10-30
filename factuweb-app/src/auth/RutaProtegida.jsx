import { Navigate, useLocation } from 'react-router-dom'
import { useAuth } from './AuthContext'

/**
 * Envuelve las pantallas que requieren sesion.
 *
 * Ya no renderiza el layout: de eso se encarga AppLayout desde el Router,
 * asi la barra lateral se monta una sola vez y no se vuelve a dibujar al
 * cambiar de pantalla.
 *
 * Si la pantalla pide un rol que el usuario no tiene, se le manda a
 * facturas en vez de mostrarle un error: es la pantalla que si le toca.
 */
export default function RutaProtegida({ rolesPermitidos, children }) {
  const { autenticado, tienePermiso } = useAuth()
  const ubicacion = useLocation()

  if (!autenticado) {
    return <Navigate to="/login" state={{ desde: ubicacion.pathname }} replace />
  }
  if (rolesPermitidos && !tienePermiso(rolesPermitidos)) {
    return <Navigate to="/facturas" replace />
  }
  return children
}
