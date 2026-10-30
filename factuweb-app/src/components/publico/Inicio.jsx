import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { publicoService } from '../../services/publicoService'
import { mensajeError } from '../../utils/alertasApi'
import { Marca } from '../ui/Marca'
import { Button, Alert, Loading } from '../ui/UI'
import Logo from '../ui/Logo'

const SERVICIOS = [
  ['01', 'Facturación en segundos',
   'Seleccione el cliente, agregue productos y el sistema calcula el IVA y el total. La factura queda guardada con su número correlativo.'],
  ['02', 'Inventario propio',
   'Cada negocio administra su propio catálogo y stock. Al facturar, las existencias se descuentan automáticamente.'],
  ['03', 'Vendedores con permisos',
   'El administrador crea cuentas para sus vendedores, que solo pueden facturar y consultar, sin tocar la configuración.'],
  ['04', 'Reportes de ventas',
   'Ventas por día, semana o mes, productos más vendidos y rendimiento por vendedor, listos para consultar.'],
  ['05', 'Facturas verificables',
   'Cada factura lleva un código de verificación que permite consultarla en línea para comprobar que es auténtica.'],
  ['06', 'Datos separados por negocio',
   'La información de cada negocio es privada. Ningún negocio ve los productos, clientes ni ventas de otro.'],
]

export default function Inicio() {
  const [negocios, setNegocios] = useState([])
  const [cargando, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    publicoService.negocios()
      .then(({ data }) => setNegocios(data))
      .catch((e) => setError(mensajeError(e)))
      .finally(() => setLoading(false))
  }, [])

  const totalFacturas = negocios.reduce((s, n) => s + (n.totalFacturas || 0), 0)

  return (
    <div className="min-h-screen">
      <div className="bg-white min-h-screen">

        <div className="bg-tinta text-white px-7 py-3.5 flex justify-between items-center gap-4 flex-wrap">
          <Marca />
          <div className="flex gap-2">
            <Link to="/login">
              <Button variant="fantasma" size="chico"
                     style={{ color: 'white', borderColor: 'rgba(255,255,255,.3)' }}>
                Iniciar sesión
              </Button>
            </Link>
            <Link to="/registro"><Button variant="oro" size="chico">Registrar mi negocio</Button></Link>
          </div>
        </div>

        <section className="px-7 md:px-12 py-16 border-b border-regla relative overflow-hidden">
          <div aria-hidden className="absolute -right-15 -top-10 w-80 h-80 opacity-15 rotate-12"
               style={{ background: 'repeating-linear-gradient(-45deg, #F2C744 0 3px, transparent 3px 13px)' }} />
          <h1 className="font-display font-extrabold text-[44px] leading-[1.03] tracking-tight max-w-[15ch] relative">
            Su negocio factura. Nosotros ponemos el sistema.
          </h1>
          <p className="text-tinta-suave mt-3.5 max-w-[50ch] text-[15.5px] relative">
            Registre su negocio y empiece a emitir facturas el mismo día. Cada negocio maneja
            su propio inventario, sus vendedores y su historial de ventas, de forma
            independiente y privada.
          </p>
          <div className="flex gap-2.5 mt-6.5 flex-wrap relative">
            <Link to="/registro"><Button variant="oro" size="grande">Registrar mi negocio gratis</Button></Link>
            <a href="#servicios"><Button variant="fantasma" size="grande">Ver cómo funciona</Button></a>
          </div>
        </section>

        <section id="servicios" className="px-7 md:px-12 py-12 border-b border-regla">
          <h2 className="font-display font-bold text-xs tracking-[0.14em] uppercase text-tinta-suave mb-5.5">
            Qué incluye la plataforma
          </h2>
          <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
            {SERVICIOS.map(([n, titulo, texto]) => (
              <article key={n} className="border-t-[3px] border-tinta pt-3">
                <span className="font-mono text-[11px] text-carbon bg-tinta px-1.5 py-0.5 rounded-sm inline-block mb-2">
                  {n}
                </span>
                <b className="font-display font-bold text-[16.5px] block mb-1">{titulo}</b>
                <p className="text-tinta-suave text-[13.5px]">{texto}</p>
              </article>
            ))}
          </div>
        </section>

        <section className="px-7 md:px-12 py-12 border-b border-regla">
          <h2 className="font-display font-bold text-xs tracking-[0.14em] uppercase text-tinta-suave mb-5.5">
            Negocios que ya facturan con FactuWeb
          </h2>

          {error && (
            <Alert type="info">
              No se pudo consultar la lista de negocios. Verifique que la API esté
              corriendo en el puerto 8080.
            </Alert>
          )}
          {cargando ? (
            <Loading texto="Consultando negocios…" />
          ) : (
            <>
              <div className="grid gap-3 sm:grid-cols-2 lg:grid-cols-4">
                {negocios.map((n) => (
                  <article key={n.idEmpresa} className="border border-regla bg-white p-3.5">
                    <Logo url={n.logoUrl} nombre={n.nombre} tamano={44}
                          className="mb-2.5" />
                    <b className="text-sm block">{n.nombre}</b>
                    <small className="text-tinta-suave text-[11.5px] block mt-0.5">{n.rubro}</small>
                    <div className="mt-2 pt-2 border-t border-regla flex justify-between
                                    text-[11.5px] text-tinta-suave">
                      <span>{n.totalProductos} productos</span>
                      <span className="num">{n.totalFacturas} facturas</span>
                    </div>
                  </article>
                ))}
              </div>
              <p className="text-tinta-suave text-xs mt-3">
                {negocios.length} negocios registrados · {totalFacturas} facturas emitidas en la plataforma
              </p>
            </>
          )}
        </section>

        <section className="px-7 md:px-12 py-12 text-center bg-tinta text-white">
          <p className="font-display font-bold text-[26px] tracking-tight mb-4">
            Registre su negocio en menos de 5 minutos.
          </p>
          <Link to="/registro"><Button variant="oro" size="grande">Registrar mi negocio</Button></Link>
        </section>
      </div>
    </div>
  )
}
