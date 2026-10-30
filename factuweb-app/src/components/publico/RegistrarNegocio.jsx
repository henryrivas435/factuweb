import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../../auth/AuthContext'
import { archivoService as apiArchivos } from '../../services/archivoService'
import { empresaService as apiEmpresa } from '../../services/empresaService'
import { mensajeError } from '../../utils/alertasApi'
import { Marca } from '../ui/Marca'
import { Button, Field, Input, Card, Alert } from '../ui/UI'
import SubirImagen from '../ui/SubirImagen'
import EntradaConSugerencias from '../ui/EntradaConSugerencias'

/* Solo son sugerencias: el rubro se escribe libremente. */
const RUBROS = ['Papelería', 'Ferretería', 'Farmacia', 'Abarrotes',
                'Repuestos', 'Panadería', 'Agropecuaria', 'Otro']

export default function RegistrarNegocio() {
  const { registrarNegocio } = useAuth()
  const navegar = useNavigate()

  const [datos, setDatos] = useState({
    nombreNegocio: '', rubro: '', nit: '', direccion: '', telefono: '',
    nombreAdmin: '', emailAdmin: '', passwordAdmin: '',
  })
  const [confirmar, setConfirmar] = useState('')
  const [logo, setLogo] = useState(null)
  const [error, setError] = useState('')
  const [enviando, setEnviando] = useState(false)

  const cambiar = (campo) => (e) =>
    setDatos((d) => ({ ...d, [campo]: e.target.value }))

  const enviar = async (e) => {
    e.preventDefault()
    setError('')

    if (datos.passwordAdmin !== confirmar) {
      setError('Las contraseñas no coinciden')
      return
    }
    setEnviando(true)
    try {
      /* Primero se crea el negocio: eso devuelve el token de la sesión.
         Solo después se puede subir el logotipo, porque la carga de
         archivos exige estar autenticado. */
      await registrarNegocio(datos)

      if (logo) {
        try {
          const { data } = await apiArchivos.subirLogo(logo)
          const actual = await apiEmpresa.obtener()
          await apiEmpresa.actualizar({ ...actual.data, logoUrl: data.url })
        } catch {
          /* Si falla la subida, el negocio ya quedó creado. El logo se
             puede agregar después desde «Datos del negocio». */
        }
      }

      navegar('/panel', { replace: true })
    } catch (err) {
      setError(mensajeError(err))
    } finally {
      setEnviando(false)
    }
  }

  return (
    <div className="min-h-screen">
      <div className="bg-white min-h-screen">
        <div className="bg-tinta text-white px-7 py-3.5 flex justify-between items-center">
          <Link to="/"><Marca /></Link>
          <Link to="/login">
            <Button variant="fantasma" size="chico"
                   style={{ color: 'white', borderColor: 'rgba(255,255,255,.3)' }}>
              Ya tengo cuenta
            </Button>
          </Link>
        </div>

        <div className="max-w-[760px] mx-auto px-6 py-8">
          <header className="pb-3.5 border-b border-regla mb-4.5">
            <h1 className="font-display font-bold text-[23px] tracking-tight">Registrar mi negocio</h1>
            <p className="text-tinta-suave text-[13px] mt-0.5">
              Cree la cuenta de su negocio y su usuario administrador
            </p>
          </header>

          <Alert>{error}</Alert>

          <form onSubmit={enviar}>
            <Card titulo="Datos del negocio" className="mb-3.5">
              <div className="grid sm:grid-cols-2 gap-x-3.5">
                <Field label="Nombre del negocio">
                  <Input value={datos.nombreNegocio} onChange={cambiar('nombreNegocio')}
                           required maxLength={100} placeholder="Ej. Librería El Progreso" />
                </Field>
                <Field label="Rubro">
                  <EntradaConSugerencias
                    value={datos.rubro} onChange={cambiar('rubro')}
                    sugerencias={RUBROS} maxLength={60}
                    placeholder="Ej. Calzado, Librería, Veterinaria…" />
                </Field>
                <Field label="NIT">
                  <Input value={datos.nit} onChange={cambiar('nit')}
                           maxLength={20} placeholder="0000-000000-000-0" />
                </Field>
                <Field label="Teléfono">
                  <Input value={datos.telefono} onChange={cambiar('telefono')}
                           maxLength={15} placeholder="0000-0000" />
                </Field>
                <Field label="Dirección" className="sm:col-span-2">
                  <Input value={datos.direccion} onChange={cambiar('direccion')}
                           maxLength={150} placeholder="Dirección del negocio" />
                </Field>
              </div>

              <SubirImagen
                valor={null}
                archivo={logo}
                onElegir={setLogo}
                etiqueta="Logotipo (opcional)"
                ayuda="Aparecerá en sus facturas y en la página pública. Puede agregarlo después. JPG o PNG, máximo 2 MB."
                ancho={110}
                alto={110}
              />
            </Card>

            <Card titulo="Usuario administrador" className="mb-3.5">
              <p className="text-tinta-suave text-[12.5px] mb-3">
                Esta será la cuenta principal del negocio. Desde ella podrá crear vendedores más adelante.
              </p>
              <div className="grid sm:grid-cols-2 gap-x-3.5">
                <Field label="Nombre completo">
                  <Input value={datos.nombreAdmin} onChange={cambiar('nombreAdmin')}
                           required maxLength={100} placeholder="Su nombre" />
                </Field>
                <Field label="Correo">
                  <Input type="email" value={datos.emailAdmin} onChange={cambiar('emailAdmin')}
                           required placeholder="correo@negocio.sv" />
                </Field>
                <Field label="Contraseña">
                  <Input type="password" value={datos.passwordAdmin}
                           onChange={cambiar('passwordAdmin')} required minLength={8}
                           placeholder="Mínimo 8 caracteres" />
                </Field>
                <Field label="Confirmar contraseña">
                  <Input type="password" value={confirmar} required
                           onChange={(e) => setConfirmar(e.target.value)} />
                </Field>
              </div>
            </Card>

            <div className="flex gap-2 items-center flex-wrap">
              <Button type="submit" size="grande" disabled={enviando}>
                {enviando ? 'Creando…' : 'Crear negocio'}
              </Button>
              <Link to="/"><Button type="button" variant="fantasma" size="grande">Cancelar</Button></Link>
              <p className="text-tinta-suave text-xs ml-auto">
                Al registrarse acepta los términos de uso.
              </p>
            </div>
          </form>
        </div>
      </div>
    </div>
  )
}
