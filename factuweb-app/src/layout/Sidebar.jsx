import { useEffect, useState } from 'react'
import { NavLink, useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'
import { empresaService } from '../services/empresaService'
import Logo from '../components/ui/Logo'
import Marca from '../components/ui/Marca'

const enlaceClase = ({ isActive }) =>
  'flex justify-between items-center gap-2 px-4.5 py-2 text-[13.5px] border-l-[3px] ' +
  (isActive
    ? 'text-white bg-white/8 border-carbon font-semibold'
    : 'text-white/70 border-transparent hover:text-white hover:bg-white/5')

function Grupo({ children }) {
  return (
    <p className="px-4.5 pt-4 pb-1.5 text-[10px] tracking-[0.12em] uppercase text-white/40">
      {children}
    </p>
  )
}

/**
 * Barra lateral: identifica el negocio del usuario y da acceso a las
 * pantallas que le corresponden segun su rol.
 */
export default function Sidebar() {
  const { sesion, esAdministrador, cerrarSesion } = useAuth()
  const navegar = useNavigate()
  const [negocio, setNegocio] = useState(null)

  /* Se consulta el negocio para mostrar su logotipo en la barra lateral. */
  useEffect(() => {
    let vigente = true
    empresaService.obtener()
      .then(({ data }) => { if (vigente) setNegocio(data) })
      .catch(() => { if (vigente) setNegocio(null) })
    // El "vigente" evita actualizar el estado si el componente ya se
    // desmonto mientras la peticion seguia en curso.
    return () => { vigente = false }
  }, [sesion?.idEmpresa])

  const salir = () => {
    cerrarSesion()
    navegar('/login')
  }

  return (
    <aside className="no-imprimir bg-tinta text-white py-4.5 md:sticky md:top-0
                      md:h-screen md:overflow-y-auto flex flex-col">
      <div className="px-4.5 pb-4 border-b border-white/12">
        <Marca />
      </div>

      <div className="px-4.5 py-2.5 my-2 bg-white/6 border-l-[3px] border-carbon
                      flex items-center gap-2.5">
        <Logo url={negocio?.logoUrl} nombre={sesion?.nombreEmpresa ?? ''} tamano={34} />
        <div className="min-w-0">
          <small className="block text-[9.5px] tracking-[0.12em] uppercase text-white/45">
            Negocio
          </small>
          <b className="text-[13px] font-semibold block leading-tight">
            {sesion?.nombreEmpresa}
          </b>
        </div>
      </div>

      <nav>
        {esAdministrador && <NavLink to="/panel" className={enlaceClase}>Panel</NavLink>}

        <Grupo>Facturación</Grupo>
        <NavLink to="/facturas/nueva" className={enlaceClase}>Nueva factura</NavLink>
        <NavLink to="/facturas" className={enlaceClase} end>Facturas</NavLink>

        <Grupo>Mi negocio</Grupo>
        <NavLink to="/stock" className={enlaceClase}>Stock</NavLink>
        <NavLink to="/clientes" className={enlaceClase}>Clientes</NavLink>

        {esAdministrador && (
          <>
            <Grupo>Administración</Grupo>
            <NavLink to="/vendedores" className={enlaceClase}>Vendedores</NavLink>
            <NavLink to="/reportes" className={enlaceClase}>Reportes</NavLink>
            <NavLink to="/negocio" className={enlaceClase}>Datos del negocio</NavLink>
          </>
        )}
      </nav>

      <div className="mt-auto px-4.5 pt-3 border-t border-white/12 text-[12px] text-white/60">
        <b className="block text-white text-[13px] font-semibold">{sesion?.nombre}</b>
        <span className="capitalize">{sesion?.rol?.toLowerCase()}</span>
        <button
          onClick={salir}
          className="block mt-2 text-carbon hover:underline text-[12px] cursor-pointer"
        >
          Cerrar sesión
        </button>
      </div>
    </aside>
  )
}
