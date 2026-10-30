import { Outlet } from 'react-router-dom'
import Sidebar from '../layout/Sidebar'
import Footer from '../layout/Footer'

/**
 * Estructura de las pantallas internas: barra lateral fija y area de
 * contenido. El <Outlet /> es donde React Router inserta la pantalla
 * de la ruta activa.
 */
export default function AppLayout() {
  return (
    <div className="min-h-screen">
      <div className="marco-impresion grid md:grid-cols-[240px_1fr] bg-white min-h-screen">
        <Sidebar />

        <main className="marco-impresion p-5 md:px-8 md:py-6 max-w-[1500px] w-full
                         flex flex-col">
          <div className="flex-1">
            <Outlet />
          </div>
          <Footer />
        </main>
      </div>
    </div>
  )
}
