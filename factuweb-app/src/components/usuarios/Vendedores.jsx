import { useEffect, useState } from 'react'
import { usuarioService as api } from '../../services/usuarioService'
import { mensajeError } from '../../utils/alertasApi'
import { useAuth } from '../../auth/AuthContext'
import { Encabezado } from '../ui/Encabezado'
import { Tabla, Fila, Celda } from '../ui/Tabla'
import { Button, Field, Input, Select, Card, Badge, Alert, Loading } from '../ui/UI'

const VACIO = { nombre: '', email: '', password: '', rol: 'VENDEDOR' }

export default function Vendedores() {
  const { sesion } = useAuth()

  const [lista, setLista] = useState([])
  const [cargando, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [exito, setExito] = useState('')

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

  const abrirEdicion = (u) => {
    setForm({ nombre: u.nombre, email: u.email, password: '', rol: u.rol })
    setEditando(u.idUsuario); setMostrar(true); setExito('')
  }

  const guardar = async (e) => {
    e.preventDefault()
    setGuardando(true); setError(''); setExito('')
    try {
      if (editando) {
        await api.actualizar(editando, form)
        setExito('Cuenta actualizada')
      } else {
        await api.crear(form)
        setExito('Cuenta creada. Ya puede iniciar sesión con esa contraseña.')
      }
      setMostrar(false); setForm(VACIO); setEditando(null)
      cargar()
    } catch (err) {
      setError(mensajeError(err))
    } finally {
      setGuardando(false)
    }
  }

  const cambiarEstado = async (u) => {
    setError(''); setExito('')
    try {
      if (u.activo) {
        await api.desactivar(u.idUsuario)
        setExito('Cuenta desactivada. Sus facturas se conservan.')
      } else {
        await api.reactivar(u.idUsuario)
        setExito('Cuenta reactivada')
      }
      cargar()
    } catch (err) {
      setError(mensajeError(err))
    }
  }

  const activas = lista.filter((u) => u.activo).length

  return (
    <>
      <Encabezado titulo="Vendedores"
                  subtitulo={`${lista.length} cuentas · ${activas} activas`}>
        <Button onClick={abrirNuevo}>Agregar vendedor</Button>
      </Encabezado>

      <Alert>{error}</Alert>
      <Alert type="exito">{exito}</Alert>

      {cargando ? <Loading /> : (
        <Tabla columnas={[
          { titulo: 'Nombre' }, { titulo: 'Correo' }, { titulo: 'Rol' },
          { titulo: 'Facturas emitidas', derecha: true }, { titulo: 'Estado' }, { titulo: '' },
        ]}>
          {lista.map((u) => (
            <Fila key={u.idUsuario} atenuada={!u.activo}>
              <Celda>
                {u.nombre}
                {u.idUsuario === sesion?.idUsuario && (
                  <span className="text-tinta-suave text-[11px]"> (usted)</span>
                )}
              </Celda>
              <Celda suave>{u.email}</Celda>
              <Celda><Badge>{u.rol}</Badge></Celda>
              <Celda mono derecha>{u.facturasEmitidas ?? 0}</Celda>
              <Celda>
                <Badge tipo={u.activo ? 'ok' : 'neutro'}>
                  {u.activo ? 'Activo' : 'Inactivo'}
                </Badge>
              </Celda>
              <Celda derecha>
                <div className="flex gap-1.5 justify-end">
                  <Button variant="fantasma" size="chico"
                         onClick={() => abrirEdicion(u)}>Editar</Button>
                  {u.idUsuario !== sesion?.idUsuario && (
                    <Button variant={u.activo ? 'peligro' : 'fantasma'} size="chico"
                           onClick={() => cambiarEstado(u)}>
                      {u.activo ? 'Desactivar' : 'Reactivar'}
                    </Button>
                  )}
                </div>
              </Celda>
            </Fila>
          ))}
        </Tabla>
      )}

      <p className="text-tinta-suave text-xs mt-2.5">
        Un vendedor puede emitir facturas y consultar el stock y los clientes, pero no
        puede modificar precios, crear usuarios ni ver los reportes del negocio.
      </p>

      {mostrar && (
        <Card titulo={editando ? 'Editar cuenta' : 'Agregar vendedor'}
              className="mt-5 max-w-[620px]">
          <form onSubmit={guardar}>
            <div className="grid sm:grid-cols-2 gap-x-3.5">
              <Field label="Nombre completo">
                <Input value={form.nombre} onChange={cambiar('nombre')}
                         required maxLength={100} placeholder="Nombre del vendedor" />
              </Field>
              <Field label="Correo">
                <Input type="email" value={form.email} onChange={cambiar('email')}
                         required placeholder="correo@negocio.sv" />
              </Field>
              <Field label={editando ? 'Nueva contraseña (opcional)' : 'Contraseña'}>
                <Input type="password" value={form.password} onChange={cambiar('password')}
                         required={!editando} minLength={8}
                         placeholder={editando ? 'Dejar vacío para no cambiarla' : 'Mínimo 8 caracteres'} />
              </Field>
              <Field label="Rol">
                <Select value={form.rol} onChange={cambiar('rol')}>
                  <option value="VENDEDOR">Vendedor</option>
                  <option value="ADMINISTRADOR">Administrador</option>
                </Select>
              </Field>
            </div>
            <div className="flex gap-2">
              <Button type="submit" disabled={guardando}>
                {guardando ? 'Guardando…' : editando ? 'Guardar cambios' : 'Crear cuenta'}
              </Button>
              <Button type="button" variant="fantasma"
                     onClick={() => { setMostrar(false); setEditando(null) }}>Cancelar</Button>
            </div>
            <p className="text-tinta-suave text-xs mt-2">
              La cuenta queda asociada únicamente a este negocio.
            </p>
          </form>
        </Card>
      )}
    </>
  )
}
