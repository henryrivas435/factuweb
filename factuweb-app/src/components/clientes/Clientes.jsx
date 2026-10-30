import { useEffect, useState } from 'react'
import { clienteService as api } from '../../services/clienteService'
import { mensajeError } from '../../utils/alertasApi'
import { useAuth } from '../../auth/AuthContext'
import { Encabezado } from '../ui/Encabezado'
import { Tabla, Fila, Celda } from '../ui/Tabla'
import { Button, Field, Input, Card, Alert, Loading, Empty } from '../ui/UI'

const VACIO = { nombre: '', duiNit: '', direccion: '', telefono: '' }

export default function Clientes() {
  const { esAdministrador } = useAuth()

  const [lista, setLista] = useState([])
  const [cargando, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [exito, setExito] = useState('')
  const [texto, setTexto] = useState('')

  const [form, setForm] = useState(VACIO)
  const [editando, setEditando] = useState(null)
  const [mostrar, setMostrar] = useState(false)
  const [guardando, setGuardando] = useState(false)

  const cargar = async () => {
    setLoading(true)
    try {
      const { data } = await api.listar()
      setLista(data); setError('')
    } catch (e) {
      setError(mensajeError(e))
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { cargar() }, [])

  const cambiar = (c) => (e) => setForm((f) => ({ ...f, [c]: e.target.value }))

  const abrirNuevo = () => { setForm(VACIO); setEditando(null); setMostrar(true); setExito('') }

  const abrirEdicion = (c) => {
    setForm({
      nombre: c.nombre, duiNit: c.duiNit ?? '',
      direccion: c.direccion ?? '', telefono: c.telefono ?? '',
    })
    setEditando(c.idCliente); setMostrar(true); setExito('')
  }

  const guardar = async (e) => {
    e.preventDefault()
    setGuardando(true); setError(''); setExito('')
    try {
      if (editando) {
        await api.actualizar(editando, form)
        setExito('Cliente actualizado')
      } else {
        await api.crear(form)
        setExito('Cliente agregado')
      }
      setMostrar(false); setForm(VACIO); setEditando(null)
      cargar()
    } catch (err) {
      setError(mensajeError(err))
    } finally {
      setGuardando(false)
    }
  }

  const eliminar = async (c) => {
    if (!confirm(`¿Eliminar a ${c.nombre}?`)) return
    setError(''); setExito('')
    try {
      await api.eliminar(c.idCliente)
      setExito('Cliente eliminado')
      cargar()
    } catch (err) {
      setError(mensajeError(err))
    }
  }

  const visibles = lista.filter((c) => {
    if (!texto) return true
    return `${c.nombre} ${c.duiNit ?? ''}`.toLowerCase().includes(texto.toLowerCase())
  })

  return (
    <>
      <Encabezado titulo="Clientes" subtitulo={`${lista.length} registrados`}>
        <Button onClick={abrirNuevo}>Agregar cliente</Button>
      </Encabezado>

      <Alert>{error}</Alert>
      <Alert type="exito">{exito}</Alert>

      <Input type="search" placeholder="Buscar por nombre o DUI" value={texto}
               onChange={(e) => setTexto(e.target.value)}
               style={{ maxWidth: 300, marginBottom: 14 }} />

      {cargando ? <Loading /> : visibles.length === 0 ? (
        <Empty>Todavía no hay clientes registrados.</Empty>
      ) : (
        <Tabla columnas={[
          { titulo: 'Nombre' }, { titulo: 'DUI / NIT' }, { titulo: 'Dirección' },
          { titulo: 'Teléfono' }, { titulo: 'Facturas', derecha: true }, { titulo: '' },
        ]}>
          {visibles.map((c) => (
            <Fila key={c.idCliente}>
              <Celda>{c.nombre}</Celda>
              <Celda mono>{c.duiNit}</Celda>
              <Celda suave>{c.direccion}</Celda>
              <Celda mono>{c.telefono}</Celda>
              <Celda mono derecha>{c.facturasEmitidas ?? 0}</Celda>
              <Celda derecha>
                <div className="flex gap-1.5 justify-end">
                  <Button variant="fantasma" size="chico"
                         onClick={() => abrirEdicion(c)}>Editar</Button>
                  {esAdministrador && (
                    <Button variant="peligro" size="chico"
                           onClick={() => eliminar(c)}>Eliminar</Button>
                  )}
                </div>
              </Celda>
            </Fila>
          ))}
        </Tabla>
      )}

      {mostrar && (
        <Card titulo={editando ? 'Editar cliente' : 'Agregar cliente'}
              className="mt-5 max-w-[620px]">
          <form onSubmit={guardar}>
            <div className="grid sm:grid-cols-2 gap-x-3.5">
              <Field label="Nombre completo">
                <Input value={form.nombre} onChange={cambiar('nombre')}
                         required maxLength={100} placeholder="Nombre del cliente" />
              </Field>
              <Field label="DUI o NIT">
                <Input value={form.duiNit} onChange={cambiar('duiNit')}
                         maxLength={20} placeholder="00000000-0" />
              </Field>
              <Field label="Dirección">
                <Input value={form.direccion} onChange={cambiar('direccion')} maxLength={150} />
              </Field>
              <Field label="Teléfono">
                <Input value={form.telefono} onChange={cambiar('telefono')}
                         maxLength={15} placeholder="0000-0000" />
              </Field>
            </div>
            <div className="flex gap-2">
              <Button type="submit" disabled={guardando}>
                {guardando ? 'Guardando…' : 'Guardar cliente'}
              </Button>
              <Button type="button" variant="fantasma"
                     onClick={() => { setMostrar(false); setEditando(null) }}>Cancelar</Button>
            </div>
          </form>
        </Card>
      )}
    </>
  )
}
