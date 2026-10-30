import { useEffect, useState, useMemo } from 'react'
import { useNavigate } from 'react-router-dom'
import { productoService as apiProductos } from '../../services/productoService'
import { clienteService as apiClientes } from '../../services/clienteService'
import { facturaService as apiFacturas } from '../../services/facturaService'
import { empresaService as apiEmpresa } from '../../services/empresaService'
import { mensajeError } from '../../utils/alertasApi'
import { useAuth } from '../../auth/AuthContext'
import { Encabezado } from '../ui/Encabezado'
import { Tabla, Fila, Celda } from '../ui/Tabla'
import { Button, Field, Input, Select, Card, Alert, Loading } from '../ui/UI'
import HojaFactura from './HojaFactura'
import { dinero } from '../../utils/formato'

const IVA = 0.13

export default function NuevaFactura() {
  const { sesion } = useAuth()
  const navegar = useNavigate()

  const [prods, setProds] = useState([])
  const [clis, setClis] = useState([])
  const [negocio, setNegocio] = useState(null)
  const [cargando, setLoading] = useState(true)

  const [idCliente, setIdCliente] = useState('')
  const [nombreCF, setNombreCF] = useState('')
  const [duiCF, setDuiCF] = useState('')

  const [idProducto, setIdProducto] = useState('')
  const [cantidad, setCantidad] = useState(1)
  const [lineas, setLineas] = useState([])

  const [error, setError] = useState('')
  const [emitiendo, setEmitiendo] = useState(false)

  useEffect(() => {
    Promise.all([apiProductos.listar(), apiClientes.listar(), apiEmpresa.obtener()])
      .then(([p, c, e]) => {
        setProds(p.data.filter((x) => x.activo))
        setClis(c.data)
        setNegocio(e.data)
      })
      .catch((err) => setError(mensajeError(err)))
      .finally(() => setLoading(false))
  }, [])

  /* Totales calculados en vivo. Al emitir, el backend los recalcula:
     estos son solo para que el vendedor vea el monto mientras arma la venta. */
  const totales = useMemo(() => {
    const subtotal = lineas.reduce((s, l) => s + l.precio * l.cantidad, 0)
    const iva = Math.round(subtotal * IVA * 100) / 100
    return {
      subtotal: Math.round(subtotal * 100) / 100,
      iva,
      total: Math.round((subtotal + iva) * 100) / 100,
    }
  }, [lineas])

  const clienteElegido = clis.find((c) => String(c.idCliente) === idCliente)

  const agregar = () => {
    setError('')
    const p = prods.find((x) => String(x.idProducto) === idProducto)
    if (!p) { setError('Seleccione un producto'); return }

    const cant = Number(cantidad)
    if (!cant || cant < 1) { setError('La cantidad debe ser al menos 1'); return }

    const yaAgregado = lineas.find((l) => l.idProducto === p.idProducto)
    const totalPedido = cant + (yaAgregado?.cantidad ?? 0)
    if (totalPedido > p.stock) {
      setError(`Stock insuficiente de ${p.nombre}. Disponible: ${p.stock}`)
      return
    }

    if (yaAgregado) {
      setLineas((ls) => ls.map((l) =>
        l.idProducto === p.idProducto ? { ...l, cantidad: totalPedido } : l))
    } else {
      setLineas((ls) => [...ls, {
        idProducto: p.idProducto, item: p.item, nombre: p.nombre,
        precio: Number(p.precio), cantidad: cant, stock: p.stock,
      }])
    }
    setCantidad(1)
  }

  const quitar = (id) => setLineas((ls) => ls.filter((l) => l.idProducto !== id))

  const emitir = async () => {
    if (lineas.length === 0) { setError('La factura debe tener al menos un producto'); return }
    setEmitiendo(true); setError('')
    try {
      const { data } = await apiFacturas.emitir({
        idCliente: idCliente ? Number(idCliente) : null,
        clienteNombre: idCliente ? null : (nombreCF || null),
        clienteDui: idCliente ? null : (duiCF || null),
        lineas: lineas.map((l) => ({ idProducto: l.idProducto, cantidad: l.cantidad })),
      })
      navegar(`/facturas/${data.idFactura}`, { state: { recienEmitida: true } })
    } catch (err) {
      setError(mensajeError(err))
    } finally {
      setEmitiendo(false)
    }
  }

  /* Vista previa: se arma con los mismos datos que se enviaran al backend. */
  const previa = {
    estado: 'EMITIDA',
    clienteNombre: clienteElegido?.nombre ?? (nombreCF || 'Consumidor final'),
    clienteDui: clienteElegido?.duiNit ?? duiCF,
    vendedor: sesion?.nombre,
    subtotal: totales.subtotal, iva: totales.iva, total: totales.total,
    detalles: lineas.map((l) => ({
      idDetalle: l.idProducto, nombre: l.nombre, cantidad: l.cantidad,
      precio: l.precio, subtotalLinea: Math.round(l.precio * l.cantidad * 100) / 100,
    })),
  }

  if (cargando) return <Loading texto="Preparando la factura…" />

  return (
    <>
      <Encabezado titulo="Nueva factura" subtitulo="Los totales se confirman al emitir">
        <Button variant="fantasma" onClick={() => setLineas([])}>Descartar</Button>
        <Button onClick={emitir} disabled={emitiendo || lineas.length === 0}>
          {emitiendo ? 'Emitiendo…' : 'Emitir factura'}
        </Button>
      </Encabezado>

      <Alert>{error}</Alert>

      <div className="grid lg:grid-cols-[1.05fr_0.95fr] gap-5 items-start">
        <div>
          <Card titulo="Cliente" className="mb-3.5">
            <Field label="Buscar cliente">
              <Select value={idCliente} onChange={(e) => setIdCliente(e.target.value)}>
                <option value="">Consumidor final</option>
                {clis.map((c) => (
                  <option key={c.idCliente} value={c.idCliente}>
                    {c.nombre}{c.duiNit ? ` — ${c.duiNit}` : ''}
                  </option>
                ))}
              </Select>
            </Field>

            {!idCliente && (
              <div className="grid sm:grid-cols-2 gap-x-3.5">
                <Field label="Nombre (opcional)">
                  <Input value={nombreCF} onChange={(e) => setNombreCF(e.target.value)}
                           placeholder="Consumidor final" maxLength={100} />
                </Field>
                <Field label="DUI (opcional)">
                  <Input value={duiCF} onChange={(e) => setDuiCF(e.target.value)}
                           placeholder="00000000-0" maxLength={20} />
                </Field>
              </div>
            )}

            <p className="text-tinta-suave text-xs">
              Al emitir, el nombre, DUI y dirección se copian a la factura para que no
              cambien si el cliente se edita después.
            </p>
          </Card>

          <Card titulo="Agregar producto">
            <div className="grid sm:grid-cols-[1fr_110px_auto] gap-x-3.5 items-end">
              <Field label="Producto del stock">
                <Select value={idProducto} onChange={(e) => setIdProducto(e.target.value)}>
                  <option value="">Seleccione…</option>
                  {prods.map((p) => (
                    <option key={p.idProducto} value={p.idProducto}>
                      {p.item ? `${p.item} · ` : ''}{p.nombre} ({p.stock} disp.)
                    </option>
                  ))}
                </Select>
              </Field>
              <Field label="Cantidad">
                <Input type="number" min="1" value={cantidad}
                         onChange={(e) => setCantidad(e.target.value)} />
              </Field>
              <Field>
                <Button type="button" onClick={agregar}>Agregar</Button>
              </Field>
            </div>

            {lineas.length > 0 && (
              <div className="mt-3">
                <Tabla columnas={[
                  { titulo: 'Item' }, { titulo: 'Producto' },
                  { titulo: 'Cant.', derecha: true }, { titulo: 'Precio', derecha: true },
                  { titulo: 'Total', derecha: true }, { titulo: '' },
                ]}>
                  {lineas.map((l) => (
                    <Fila key={l.idProducto}>
                      <Celda mono>{l.item}</Celda>
                      <Celda>{l.nombre}</Celda>
                      <Celda mono derecha>{l.cantidad}</Celda>
                      <Celda mono derecha>{dinero(l.precio)}</Celda>
                      <Celda mono derecha>{dinero(l.precio * l.cantidad)}</Celda>
                      <Celda derecha>
                        <Button variant="fantasma" size="chico"
                               onClick={() => quitar(l.idProducto)}>Quitar</Button>
                      </Celda>
                    </Fila>
                  ))}
                </Tabla>

                <div className="ml-auto w-[230px] text-[13px] mt-3">
                  <div className="flex justify-between py-0.5">
                    <span className="text-tinta-suave">Subtotal</span>
                    <span className="num">{dinero(totales.subtotal)}</span>
                  </div>
                  <div className="flex justify-between py-0.5">
                    <span className="text-tinta-suave">IVA (13 %)</span>
                    <span className="num">{dinero(totales.iva)}</span>
                  </div>
                  <div className="flex justify-between pt-2 mt-1 border-t-[1.5px] border-tinta
                                  font-display font-bold text-[17px]">
                    <span>Total</span>
                    <span className="num">{dinero(totales.total)}</span>
                  </div>
                </div>
              </div>
            )}

            <p className="text-tinta-suave text-xs mt-2.5">
              Al emitir, el stock de cada producto se descuenta automáticamente.
            </p>
          </Card>
        </div>

        <HojaFactura factura={previa} empresa={negocio} previa />
      </div>
    </>
  )
}
