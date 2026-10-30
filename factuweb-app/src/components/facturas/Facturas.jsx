import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { facturaService as apiFacturas } from '../../services/facturaService'
import { mensajeError } from '../../utils/alertasApi'
import { Encabezado } from '../ui/Encabezado'
import { Tabla, Fila, Celda } from '../ui/Tabla'
import { Button, Input, Select, Badge, Alert, Loading, Empty } from '../ui/UI'
import { dinero, fecha, hoyISO, inicioMesISO } from '../../utils/formato'

const selloDe = (e) => (e === 'EMITIDA' ? 'ok' : e === 'ANULADA' ? 'no' : 'neutro')

export default function Facturas() {
  const [lista, setLista] = useState([])
  const [cargando, setLoading] = useState(true)
  const [error, setError] = useState('')

  const [texto, setTexto] = useState('')
  const [desde, setDesde] = useState(inicioMesISO())
  const [hasta, setHasta] = useState(hoyISO())
  const [estado, setEstado] = useState('')

  const cargar = async () => {
    setLoading(true)
    try {
      const { data } = await apiFacturas.listar()
      setLista(data)
      setError('')
    } catch (e) {
      setError(mensajeError(e))
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { cargar() }, [])

  const filtrar = async () => {
    setLoading(true)
    try {
      const { data } = await apiFacturas.porPeriodo(desde, hasta, estado || undefined)
      setLista(data)
      setError('')
    } catch (e) {
      setError(mensajeError(e))
    } finally {
      setLoading(false)
    }
  }

  const visibles = lista.filter((f) => {
    if (!texto) return true
    const t = `${f.numeroFactura} ${f.clienteNombre ?? ''}`.toLowerCase()
    return t.includes(texto.toLowerCase())
  })

  const emitidas = lista.filter((f) => f.estado === 'EMITIDA').length
  const anuladas = lista.filter((f) => f.estado === 'ANULADA').length

  return (
    <>
      <Encabezado titulo="Facturas"
                  subtitulo={`${emitidas} emitidas · ${anuladas} anuladas`}>
        <Link to="/facturas/nueva"><Button>Nueva factura</Button></Link>
      </Encabezado>

      <Alert>{error}</Alert>

      <div className="flex gap-2.5 mb-3.5 flex-wrap items-end">
        <Input type="search" placeholder="N.º de factura o cliente" value={texto}
                 onChange={(e) => setTexto(e.target.value)} style={{ maxWidth: 230 }} />
        <Input type="date" value={desde} onChange={(e) => setDesde(e.target.value)}
                 style={{ maxWidth: 155 }} />
        <Input type="date" value={hasta} onChange={(e) => setHasta(e.target.value)}
                 style={{ maxWidth: 155 }} />
        <Select value={estado} onChange={(e) => setEstado(e.target.value)}
                  style={{ maxWidth: 150 }}>
          <option value="">Todos los estados</option>
          <option value="EMITIDA">Emitida</option>
          <option value="ANULADA">Anulada</option>
          <option value="ELIMINADA">Eliminada</option>
        </Select>
        <Button variant="fantasma" onClick={filtrar}>Filtrar</Button>
        <Button variant="fantasma" onClick={cargar}>Ver todas</Button>
      </div>

      {cargando ? <Loading /> : visibles.length === 0 ? (
        <Empty>No hay facturas en el período seleccionado.</Empty>
      ) : (
        <Tabla columnas={[
          { titulo: 'N.º' }, { titulo: 'Fecha' }, { titulo: 'Cliente' },
          { titulo: 'Vendedor' }, { titulo: 'Total', derecha: true },
          { titulo: 'Estado' }, { titulo: '' },
        ]}>
          {visibles.map((f) => (
            <Fila key={f.idFactura} atenuada={f.estado === 'ELIMINADA'}>
              <Celda mono>{f.numeroFactura}</Celda>
              <Celda mono suave>{fecha(f.fechaEmision)}</Celda>
              <Celda>{f.clienteNombre}</Celda>
              <Celda suave>{f.vendedor}</Celda>
              <Celda mono derecha>{dinero(f.total)}</Celda>
              <Celda><Badge tipo={selloDe(f.estado)}>{f.estado}</Badge></Celda>
              <Celda derecha>
                <Link to={`/facturas/${f.idFactura}`}>
                  <Button variant="fantasma" size="chico">Ver</Button>
                </Link>
              </Celda>
            </Fila>
          ))}
        </Tabla>
      )}

      <p className="text-tinta-suave text-xs mt-2.5">
        Una factura nunca se borra de la base de datos: cambia de estado y conserva
        su detalle para auditoría.
      </p>
    </>
  )
}
