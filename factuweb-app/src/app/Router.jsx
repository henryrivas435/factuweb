import { Routes, Route, Navigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'
import RutaProtegida from '../auth/RutaProtegida'
import AppLayout from './AppLayout'

import Inicio from '../components/publico/Inicio'
import RegistrarNegocio from '../components/publico/RegistrarNegocio'
import VerificarFactura from '../components/publico/VerificarFactura'
import Login from '../auth/Login'

import Dashboard from '../components/Dashboard'
import Stock from '../components/productos/Stock'
import Clientes from '../components/clientes/Clientes'
import Vendedores from '../components/usuarios/Vendedores'
import NuevaFactura from '../components/facturas/NuevaFactura'
import Facturas from '../components/facturas/Facturas'
import DetalleFactura from '../components/facturas/DetalleFactura'
import MiNegocio from '../components/empresa/MiNegocio'
import Reportes from '../reports/Reportes'

const SOLO_ADMIN = ['ADMINISTRADOR']

/**
 * Mapa de rutas.
 *
 * Las cuatro primeras son publicas. El resto cuelga de una ruta padre que
 * exige sesion y monta AppLayout una sola vez; dentro, cada pantalla
 * declara con RutaProtegida los roles que la pueden ver.
 */
export default function Router() {
  const { autenticado, esAdministrador } = useAuth()
  const inicioSesion = esAdministrador ? '/panel' : '/facturas/nueva'

  return (
    <Routes>
      {/* Publicas */}
      <Route path="/" element={<Inicio />} />
      <Route
        path="/registro"
        element={autenticado ? <Navigate to={inicioSesion} replace /> : <RegistrarNegocio />}
      />
      <Route
        path="/login"
        element={autenticado ? <Navigate to={inicioSesion} replace /> : <Login />}
      />
      <Route path="/verificar/:numero" element={<VerificarFactura />} />

      {/* Privadas: la sesion se valida una vez aqui, en la ruta padre */}
      <Route
        element={
          <RutaProtegida>
            <AppLayout />
          </RutaProtegida>
        }
      >
        {/* Solo administrador */}
        <Route
          path="/panel"
          element={
            <RutaProtegida rolesPermitidos={SOLO_ADMIN}>
              <Dashboard />
            </RutaProtegida>
          }
        />
        <Route
          path="/vendedores"
          element={
            <RutaProtegida rolesPermitidos={SOLO_ADMIN}>
              <Vendedores />
            </RutaProtegida>
          }
        />
        <Route
          path="/reportes"
          element={
            <RutaProtegida rolesPermitidos={SOLO_ADMIN}>
              <Reportes />
            </RutaProtegida>
          }
        />
        <Route
          path="/negocio"
          element={
            <RutaProtegida rolesPermitidos={SOLO_ADMIN}>
              <MiNegocio />
            </RutaProtegida>
          }
        />

        {/* Administrador y vendedor */}
        <Route path="/stock" element={<Stock />} />
        <Route path="/clientes" element={<Clientes />} />
        <Route path="/facturas/nueva" element={<NuevaFactura />} />
        <Route path="/facturas/:id" element={<DetalleFactura />} />
        <Route path="/facturas" element={<Facturas />} />
      </Route>

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}
