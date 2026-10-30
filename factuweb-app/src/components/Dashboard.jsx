import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { reporteService } from '../services/reporteService'
import { mensajeError } from '../utils/alertasApi'
import { Encabezado } from './ui/Encabezado'
import { Button, Card, Indicator, Badge, Loading, Alert, Empty } from './ui/UI'
import { dinero, fecha } from '../utils/formato'

const selloDe = (estado) =>
  estado === 'EMITIDA' ? 'ok' : estado === 'ANULADA' ? 'no' : 'neutro'

export default function Panel() {
  const [datos, setDatos] = useState(null)
  const [error, setError] = useState('')

  useEffect(() => {
    reporteService.panel()
      .then(({ data }) => setDatos(data))
      .catch((e) => setError(mensajeError(e)))
  }, [])

  const hoy = new Date().toLocaleDateString('es-SV', {
    weekday: 'long', day: 'numeric', month: 'long',
  })

  return (
    <>
      <Encabezado titulo="Panel" subtitulo={`Resumen del ${hoy}`}>
        <Link to="/facturas/nueva"><Button>Nueva factura</Button></Link>
      </Encabezado>

      <Alert>{error}</Alert>
      {!datos && !error && <Loading />}

      {datos && (
        <>
          <dl className="grid gap-3 sm:grid-cols-2 lg:grid-cols-4 mb-5">
            <Indicator tono="bueno" etiqueta="Ventas de hoy"
                       valor={dinero(datos.ventasHoy)}
                       nota={`${datos.facturasHoy} facturas emitidas`} />
            <Indicator etiqueta="Ventas del mes"
                       valor={dinero(datos.ventasMes)}
                       nota={`${datos.facturasMes} facturas`} />
            <Indicator etiqueta="Vendedores activos"
                       valor={datos.vendedoresActivos}
                       nota="cuentas habilitadas" />
            <Indicator tono={datos.productosBajoMinimo > 0 ? 'alerta' : 'normal'}
                       etiqueta="Productos bajo mínimo"
                       valor={datos.productosBajoMinimo}
                       nota={datos.productosBajoMinimo > 0
                         ? 'Requieren reabastecerse' : 'Todo en orden'} />
          </dl>

          <Card titulo="Últimas facturas">
            {datos.ultimasFacturas?.length ? (
              <div className="overflow-x-auto">
                <table className="w-full text-[13px]">
                  <thead>
                    <tr className="border-b-[1.5px] border-tinta text-left">
                      <th className="py-2 px-2.5 text-[10.5px] tracking-wider uppercase text-tinta-suave font-semibold">N.º</th>
                      <th className="py-2 px-2.5 text-[10.5px] tracking-wider uppercase text-tinta-suave font-semibold">Fecha</th>
                      <th className="py-2 px-2.5 text-[10.5px] tracking-wider uppercase text-tinta-suave font-semibold">Cliente</th>
                      <th className="py-2 px-2.5 text-[10.5px] tracking-wider uppercase text-tinta-suave font-semibold">Vendedor</th>
                      <th className="py-2 px-2.5 text-[10.5px] tracking-wider uppercase text-tinta-suave font-semibold text-right">Total</th>
                      <th className="py-2 px-2.5 text-[10.5px] tracking-wider uppercase text-tinta-suave font-semibold">Estado</th>
                    </tr>
                  </thead>
                  <tbody>
                    {datos.ultimasFacturas.map((f) => (
                      <tr key={f.idFactura} className="border-b border-regla hover:bg-papel">
                        <td className="py-2 px-2.5 num">{f.numeroFactura}</td>
                        <td className="py-2 px-2.5 num text-tinta-suave">{fecha(f.fechaEmision)}</td>
                        <td className="py-2 px-2.5">{f.clienteNombre}</td>
                        <td className="py-2 px-2.5 text-tinta-suave">{f.vendedor}</td>
                        <td className="py-2 px-2.5 num text-right">{dinero(f.total)}</td>
                        <td className="py-2 px-2.5"><Badge tipo={selloDe(f.estado)}>{f.estado}</Badge></td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            ) : (
              <Empty>Todavía no se ha emitido ninguna factura.</Empty>
            )}
          </Card>
        </>
      )}
    </>
  )
}
