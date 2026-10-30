import { useEffect, useState, useCallback } from 'react'
import { productoService as apiProductos } from '../../services/productoService'
import { categoriaService as apiCategorias } from '../../services/categoriaService'
import { archivoService as apiArchivos } from '../../services/archivoService'
import { mensajeError } from '../../utils/alertasApi'
import { useAuth } from '../../auth/AuthContext'
import { Encabezado } from '../ui/Encabezado'
import { Tabla, Fila, Celda } from '../ui/Tabla'
import { Button, Field, Input, Select, Card, Badge, Alert, Loading, Empty } from '../ui/UI'
import { dinero } from '../../utils/formato'
import SubirImagen from '../ui/SubirImagen'

const FORM_VACIO = {
  item: '', nombre: '', descripcion: '', precio: '',
  stock: '', stockMinimo: '', idCategoria: '', imagenUrl: '',
}

export default function Stock() {
  const { esAdministrador } = useAuth()

  const [lista, setLista] = useState([])
  const [cats, setCats] = useState([])
  const [cargando, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [exito, setExito] = useState('')

  const [busqueda, setBusqueda] = useState('')
  const [verInactivos, setVerInactivos] = useState(false)
  const [soloBajoMinimo, setSoloBajoMinimo] = useState(false)

  const [form, setForm] = useState(FORM_VACIO)
  const [imagenNueva, setImagenNueva] = useState(null)
  const [quitarImagen, setQuitarImagen] = useState(false)
  const [editando, setEditando] = useState(null)
  const [mostrarForm, setMostrarForm] = useState(false)
  const [guardando, setGuardando] = useState(false)

  const cargar = useCallback(async () => {
    setLoading(true)
    try {
      const [p, c] = await Promise.all([
        apiProductos.listar(verInactivos),
        apiCategorias.listar(),
      ])
      setLista(p.data)
      setCats(c.data)
      setError('')
    } catch (e) {
      setError(mensajeError(e))
    } finally {
      setLoading(false)
    }
  }, [verInactivos])

  useEffect(() => { cargar() }, [cargar])

  const visibles = lista.filter((p) => {
    const texto = `${p.item ?? ''} ${p.nombre}`.toLowerCase()
    if (busqueda && !texto.includes(busqueda.toLowerCase())) return false
    if (soloBajoMinimo && !(p.stock <= p.stockMinimo)) return false
    return true
  })

  const cambiar = (campo) => (e) => setForm((f) => ({ ...f, [campo]: e.target.value }))

  const abrirNuevo = () => {
    setForm(FORM_VACIO); setEditando(null); setMostrarForm(true); setExito('')
    setImagenNueva(null); setQuitarImagen(false)
  }

  const abrirEdicion = (p) => {
    setForm({
      item: p.item ?? '', nombre: p.nombre, descripcion: p.descripcion ?? '',
      precio: p.precio, stock: p.stock, stockMinimo: p.stockMinimo,
      idCategoria: p.idCategoria ?? '', imagenUrl: p.imagenUrl ?? '',
    })
    setEditando(p.idProducto); setMostrarForm(true); setExito('')
    setImagenNueva(null); setQuitarImagen(false)
  }

  const guardar = async (e) => {
    e.preventDefault()
    setGuardando(true); setError(''); setExito('')
    try {
      /* Si eligió una foto nueva, primero se sube y se guarda su ruta. */
      let imagenUrl = form.imagenUrl || null
      if (imagenNueva) {
        const { data } = await apiArchivos.subirImagenProducto(imagenNueva)
        imagenUrl = data.url
      } else if (quitarImagen) {
        imagenUrl = null
      }

      const cuerpo = {
        ...form,
        precio: Number(form.precio),
        stock: Number(form.stock),
        stockMinimo: Number(form.stockMinimo || 0),
        idCategoria: form.idCategoria ? Number(form.idCategoria) : null,
        imagenUrl,
      }

      if (editando) {
        await apiProductos.actualizar(editando, cuerpo)
        setExito('Producto actualizado')
      } else {
        await apiProductos.crear(cuerpo)
        setExito('Producto agregado al stock')
      }
      setMostrarForm(false); setForm(FORM_VACIO); setEditando(null)
      setImagenNueva(null); setQuitarImagen(false)
      cargar()
    } catch (err) {
      setError(mensajeError(err))
    } finally {
      setGuardando(false)
    }
  }

  const cambiarEstado = async (p) => {
    setError(''); setExito('')
    try {
      if (p.activo) {
        await apiProductos.desactivar(p.idProducto)
        setExito('Producto marcado como inactivo. Las facturas emitidas conservan su información.')
      } else {
        await apiProductos.reactivar(p.idProducto)
        setExito('Producto reactivado')
      }
      cargar()
    } catch (err) {
      setError(mensajeError(err))
    }
  }

  const bajoMinimo = lista.filter((p) => p.activo && p.stock <= p.stockMinimo).length

  return (
    <>
      <Encabezado
        titulo="Stock"
        subtitulo={`${lista.filter((p) => p.activo).length} productos activos · ${bajoMinimo} bajo el mínimo`}
      >
        {esAdministrador && <Button onClick={abrirNuevo}>Agregar producto</Button>}
      </Encabezado>

      <Alert>{error}</Alert>
      <Alert type="exito">{exito}</Alert>

      <div className="flex gap-2.5 mb-3.5 flex-wrap">
        <Input type="search" placeholder="Buscar por nombre o item"
                 value={busqueda} onChange={(e) => setBusqueda(e.target.value)}
                 style={{ maxWidth: 270 }} />
        <Select value={soloBajoMinimo ? 'bajo' : 'todo'} style={{ maxWidth: 170 }}
                  onChange={(e) => setSoloBajoMinimo(e.target.value === 'bajo')}>
          <option value="todo">Todo el stock</option>
          <option value="bajo">Bajo el mínimo</option>
        </Select>
        {esAdministrador && (
          <Select value={verInactivos ? 'todos' : 'activos'} style={{ maxWidth: 180 }}
                    onChange={(e) => setVerInactivos(e.target.value === 'todos')}>
            <option value="activos">Solo activos</option>
            <option value="todos">Incluir inactivos</option>
          </Select>
        )}
      </div>

      {cargando ? <Loading /> : visibles.length === 0 ? (
        <Empty>No hay productos que coincidan con la búsqueda.</Empty>
      ) : (
        <Tabla columnas={[
          { titulo: '' }, { titulo: 'Item' }, { titulo: 'Producto' }, { titulo: 'Categoría' },
          { titulo: 'Precio', derecha: true }, { titulo: 'Stock', derecha: true },
          { titulo: 'Mínimo', derecha: true }, { titulo: 'Estado' }, { titulo: '' },
        ]}>
          {visibles.map((p) => {
            const escaso = p.activo && p.stock <= p.stockMinimo
            return (
              <Fila key={p.idProducto} atenuada={!p.activo}>
                <Celda>
                  {p.imagenUrl ? (
                    <img src={p.imagenUrl} alt=""
                         className="w-9 h-9 object-contain border border-regla bg-white" />
                  ) : (
                    <div className="w-9 h-9 border border-dashed border-regla" aria-hidden />
                  )}
                </Celda>
                <Celda mono>{p.item}</Celda>
                <Celda>{p.nombre}</Celda>
                <Celda suave>{p.nombreCategoria ?? '—'}</Celda>
                <Celda mono derecha>{dinero(p.precio)}</Celda>
                <Celda mono derecha className={escaso ? 'text-sello font-semibold' : ''}>
                  {p.stock}
                </Celda>
                <Celda mono derecha suave>{p.stockMinimo}</Celda>
                <Celda>
                  <Badge tipo={p.activo ? 'ok' : 'neutro'}>
                    {p.activo ? 'Activo' : 'Inactivo'}
                  </Badge>
                </Celda>
                <Celda derecha>
                  {esAdministrador && (
                    <div className="flex gap-1.5 justify-end">
                      {p.activo && (
                        <Button variant="fantasma" size="chico"
                               onClick={() => abrirEdicion(p)}>Editar</Button>
                      )}
                      <Button variant={p.activo ? 'peligro' : 'fantasma'} size="chico"
                             onClick={() => cambiarEstado(p)}>
                        {p.activo ? 'Dar de baja' : 'Reactivar'}
                      </Button>
                    </div>
                  )}
                </Celda>
              </Fila>
            )
          })}
        </Tabla>
      )}

      <p className="text-tinta-suave text-xs mt-2.5">
        Los productos no se eliminan: se marcan como inactivos para que las facturas
        ya emitidas conserven su información.
      </p>

      {mostrarForm && esAdministrador && (
        <Card titulo={editando ? 'Editar producto' : 'Agregar producto'} className="mt-5">
          <form onSubmit={guardar}>
            <div className="grid sm:grid-cols-2 gap-x-3.5">
              <Field label="Item">
                <Input value={form.item} onChange={cambiar('item')}
                         maxLength={20} placeholder="P-0000" />
              </Field>
              <Field label="Categoría">
                <Select value={form.idCategoria} onChange={cambiar('idCategoria')}>
                  <option value="">Sin categoría</option>
                  {cats.map((c) => (
                    <option key={c.idCategoria} value={c.idCategoria}>{c.nombre}</option>
                  ))}
                </Select>
              </Field>
              <Field label="Nombre" className="sm:col-span-2">
                <Input value={form.nombre} onChange={cambiar('nombre')}
                         required maxLength={100} placeholder="Nombre del producto" />
              </Field>
              <Field label="Descripción" className="sm:col-span-2">
                <Input value={form.descripcion} onChange={cambiar('descripcion')}
                         maxLength={255} />
              </Field>
              <Field label="Precio">
                <Input type="number" step="0.01" min="0.01" required
                         value={form.precio} onChange={cambiar('precio')} placeholder="0.00" />
              </Field>
              <Field label="Stock">
                <Input type="number" min="0" required
                         value={form.stock} onChange={cambiar('stock')} placeholder="0" />
              </Field>
              <Field label="Stock mínimo">
                <Input type="number" min="0"
                         value={form.stockMinimo} onChange={cambiar('stockMinimo')} placeholder="0" />
              </Field>
            </div>

            <SubirImagen
              valor={form.imagenUrl}
              archivo={imagenNueva}
              onElegir={(a) => { setImagenNueva(a); setQuitarImagen(a === null) }}
              etiqueta="Foto del producto"
              ayuda="Opcional. Se muestra en el catálogo. JPG o PNG, máximo 2 MB."
              ancho={100}
              alto={100}
            />
            <div className="flex gap-2">
              <Button type="submit" disabled={guardando}>
                {guardando ? 'Guardando…' : 'Guardar producto'}
              </Button>
              <Button type="button" variant="fantasma"
                     onClick={() => { setMostrarForm(false); setEditando(null) }}>
                Cancelar
              </Button>
            </div>
          </form>
        </Card>
      )}
    </>
  )
}
