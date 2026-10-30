import { useState, useEffect } from 'react'
import { reporteService as api } from '../services/reporteService'
import { empresaService } from '../services/empresaService'
import { useAuth } from '../auth/AuthContext'
import { descargarReportePDF } from '../utils/reportePDF'
import { mensajeError } from '../utils/alertasApi'
import { Encabezado } from '../components/ui/Encabezado'
import { Tabla, Fila, Celda } from '../components/ui/Tabla'
import { Button, Input, Select, Card, Indicator, Alert, Empty } from '../components/ui/UI'
import { dinero, fecha, hoyISO, inicioMesISO } from '../utils/formato'

const TIPOS = [
  ['ventas_por_periodo', 'Ventas por período'],
  ['productos_mas_vendidos', 'Productos más vendidos'],
  ['ventas_por_vendedor', 'Ventas por vendedor'],
]

export default function Reportes() {
  const { sesion } = useAuth()
  const [negocio, setNegocio] = useState(null)
  const [tipo, setTipo] = useState('ventas_por_periodo')
  const [desde, setDesde] = useState(inicioMesISO())
  const [hasta, setHasta] = useState(hoyISO())

  const [datos, setDatos] = useState(null)
  const [historial, setHistorial] = useState([])
  const [error, setError] = useState('')
  const [generando, setGenerando] = useState(false)

  /* Los datos del negocio se usan en el encabezado del PDF. */
  useEffect(() => {
    empresaService.obtener()
      .then(({ data }) => setNegocio(data))
      .catch(() => setNegocio(null))
  }, [])

  const descargarPDF = () => {
    descargarReportePDF({
      datos,
      negocio,
      periodo: { tipo, desde, hasta },
      usuario: sesion?.nombre,
    })
  }

  const generar = async () => {
    setGenerando(true); setError('')
    try {
      const { data } = await api.generar(tipo, desde, hasta)
      setDatos(data)
      const h = await api.historial()
      setHistorial(h.data)
    } catch (e) {
      setError(mensajeError(e))
    } finally {
      setGenerando(false)
    }
  }

  return (
    <>
      <Encabezado titulo="Reportes"
                  subtitulo={datos ? `Del ${fecha(desde)} al ${fecha(hasta)}` : 'Seleccione un período y genere el reporte'}>
        {datos && (
          <>
            <Button variant="fantasma" onClick={descargarPDF}>Descargar PDF</Button>
            <Button variant="fantasma" onClick={() => window.print()}>Imprimir</Button>
          </>
        )}
        <Button onClick={generar} disabled={generando}>
          {generando ? 'Generando…' : 'Generar reporte'}
        </Button>
      </Encabezado>

      <Alert>{error}</Alert>

      <div className="flex gap-2.5 mb-4.5 flex-wrap">
        <Select value={tipo} onChange={(e) => setTipo(e.target.value)} style={{ maxWidth: 215 }}>
          {TIPOS.map(([v, t]) => <option key={v} value={v}>{t}</option>)}
        </Select>
        <Input type="date" value={desde} onChange={(e) => setDesde(e.target.value)}
                 style={{ maxWidth: 160 }} />
        <Input type="date" value={hasta} onChange={(e) => setHasta(e.target.value)}
                 style={{ maxWidth: 160 }} />
      </div>

      {!datos ? (
        <Empty>Elija el tipo de reporte y el período, luego presione «Generar reporte».</Empty>
      ) : (
        <>
          <dl className="grid gap-3 sm:grid-cols-3 mb-5">
            <Indicator tono="bueno" etiqueta="Total facturado"
                       valor={dinero(datos.totalFacturado)}
                       nota={`${datos.facturasEmitidas} facturas emitidas`} />
            <Indicator etiqueta="IVA recaudado" valor={dinero(datos.ivaRecaudado)}
                       nota="13 % sobre subtotal" />
            <Indicator etiqueta="Unidades vendidas" valor={datos.unidadesVendidas}
                       nota={`en ${datos.productosMasVendidos?.length ?? 0} productos distintos`} />
          </dl>

          <div className="grid lg:grid-cols-2 gap-5 items-start">
            <Card titulo="Productos más vendidos">
              {datos.productosMasVendidos?.length ? (
                <Tabla columnas={[
                  { titulo: 'Item' }, { titulo: 'Producto' },
                  { titulo: 'Unidades', derecha: true }, { titulo: 'Ingresos', derecha: true },
                ]}>
                  {datos.productosMasVendidos.map((p, i) => (
                    <Fila key={i}>
                      <Celda mono>{p.item}</Celda>
                      <Celda>{p.nombre}</Celda>
                      <Celda mono derecha>{p.unidades}</Celda>
                      <Celda mono derecha>{dinero(p.ingresos)}</Celda>
                    </Fila>
                  ))}
                </Tabla>
              ) : <Empty>Sin ventas en el período.</Empty>}
            </Card>

            <Card titulo="Ventas por vendedor">
              {datos.ventasPorVendedor?.length ? (
                <Tabla columnas={[
                  { titulo: 'Vendedor' }, { titulo: 'Facturas', derecha: true },
                  { titulo: 'Total', derecha: true },
                ]}>
                  {datos.ventasPorVendedor.map((v, i) => (
                    <Fila key={i}>
                      <Celda>{v.vendedor}</Celda>
                      <Celda mono derecha>{v.facturas}</Celda>
                      <Celda mono derecha>{dinero(v.total)}</Celda>
                    </Fila>
                  ))}
                </Tabla>
              ) : <Empty>Sin ventas en el período.</Empty>}
            </Card>
          </div>

          {historial.length > 0 && (
            <Card titulo="Reportes generados" className="mt-5">
              <Tabla columnas={[
                { titulo: 'Tipo' }, { titulo: 'Período' },
                { titulo: 'Generó' }, { titulo: 'Fecha', derecha: true },
              ]}>
                {historial.slice(0, 8).map((r) => (
                  <Fila key={r.idReporte}>
                    <Celda>{r.tipoReporte?.replace(/_/g, ' ')}</Celda>
                    <Celda mono suave>{fecha(r.fechaInicio)} – {fecha(r.fechaFin)}</Celda>
                    <Celda suave>{r.generadoPor}</Celda>
                    <Celda mono derecha suave>{fecha(r.fechaGeneracion)}</Celda>
                  </Fila>
                ))}
              </Tabla>
              <p className="text-tinta-suave text-xs mt-2.5">
                Cada reporte generado queda registrado con su tipo, período y usuario.
              </p>
            </Card>
          )}
        </>
      )}
    </>
  )
}
