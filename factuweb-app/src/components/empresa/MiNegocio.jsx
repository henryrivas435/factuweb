import { useEffect, useState } from 'react'
import { empresaService as apiEmpresa } from '../../services/empresaService'
import { archivoService as apiArchivos } from '../../services/archivoService'
import { mensajeError } from '../../utils/alertasApi'
import { Encabezado } from '../ui/Encabezado'
import { Button, Field, Input, Card, Alert, Loading } from '../ui/UI'
import SubirImagen from '../ui/SubirImagen'
import EntradaConSugerencias from '../ui/EntradaConSugerencias'
import Logo from '../ui/Logo'
import { fecha } from '../../utils/formato'

/* Solo son sugerencias: el rubro se escribe libremente. */
const RUBROS = ['Papelería', 'Ferretería', 'Farmacia', 'Abarrotes',
                'Repuestos', 'Panadería', 'Agropecuaria', 'Otro']

/**
 * Datos del propio negocio: nombre, NIT, dirección y logotipo.
 * Es lo que aparece en el encabezado de cada factura que emite.
 */
export default function MiNegocio() {
  const [datos, setDatos] = useState(null)
  const [logoNuevo, setLogoNuevo] = useState(null)
  const [quitarLogo, setQuitarLogo] = useState(false)

  const [error, setError] = useState('')
  const [exito, setExito] = useState('')
  const [guardando, setGuardando] = useState(false)

  useEffect(() => {
    apiEmpresa.obtener()
      .then(({ data }) => setDatos(data))
      .catch((e) => setError(mensajeError(e)))
  }, [])

  const cambiar = (campo) => (e) =>
    setDatos((d) => ({ ...d, [campo]: e.target.value }))

  const elegirLogo = (archivo) => {
    setLogoNuevo(archivo)
    setQuitarLogo(archivo === null)
    setExito('')
  }

  const guardar = async (e) => {
    e.preventDefault()
    setGuardando(true); setError(''); setExito('')

    try {
      let logoUrl = datos.logoUrl

      /* Si eligió una imagen nueva, primero se sube y luego se guarda
         la ruta junto con el resto de los datos. */
      if (logoNuevo) {
        const { data } = await apiArchivos.subirLogo(logoNuevo)
        logoUrl = data.url
      } else if (quitarLogo) {
        logoUrl = null
      }

      const { data } = await apiEmpresa.actualizar({ ...datos, logoUrl })
      setDatos(data)
      setLogoNuevo(null)
      setQuitarLogo(false)
      setExito('Datos del negocio actualizados. El logotipo ya aparece en las facturas.')
    } catch (err) {
      setError(mensajeError(err))
    } finally {
      setGuardando(false)
    }
  }

  if (!datos && !error) return <Loading />

  return (
    <>
      <Encabezado
        titulo="Datos del negocio"
        subtitulo={datos?.fechaRegistro
          ? `Registrado el ${fecha(datos.fechaRegistro)}` : undefined}
      />

      <Alert>{error}</Alert>
      <Alert type="exito">{exito}</Alert>

      {datos && (
        <form onSubmit={guardar} className="grid lg:grid-cols-[1fr_320px] gap-5 items-start">
          <div>
            <Card titulo="Identificación" className="mb-3.5">
              <div className="grid sm:grid-cols-2 gap-x-3.5">
                <Field label="Nombre del negocio">
                  <Input value={datos.nombre ?? ''} onChange={cambiar('nombre')}
                           required maxLength={100} />
                </Field>
                <Field label="Rubro">
                  <EntradaConSugerencias
                    value={datos.rubro ?? ''} onChange={cambiar('rubro')}
                    sugerencias={RUBROS} maxLength={60}
                    placeholder="Ej. Calzado, Librería, Veterinaria…" />
                </Field>
                <Field label="NIT">
                  <Input value={datos.nit ?? ''} onChange={cambiar('nit')}
                           maxLength={20} placeholder="0000-000000-000-0" />
                </Field>
                <Field label="Teléfono">
                  <Input value={datos.telefono ?? ''} onChange={cambiar('telefono')}
                           maxLength={15} placeholder="0000-0000" />
                </Field>
                <Field label="Dirección" className="sm:col-span-2">
                  <Input value={datos.direccion ?? ''} onChange={cambiar('direccion')}
                           maxLength={150} />
                </Field>
              </div>
              <p className="text-tinta-suave text-xs">
                Estos datos se imprimen en el encabezado de cada factura que emita
                el negocio.
              </p>
            </Card>

            <Card titulo="Logotipo">
              <SubirImagen
                valor={datos.logoUrl}
                archivo={logoNuevo}
                onElegir={elegirLogo}
                etiqueta="Imagen del logotipo"
                ayuda="Se verá en las facturas, en la página pública y en el menú. Ideal cuadrada, JPG o PNG, máximo 2 MB."
                ancho={140}
                alto={140}
              />
            </Card>

            <div className="flex gap-2 mt-4">
              <Button type="submit" size="grande" disabled={guardando}>
                {guardando ? 'Guardando…' : 'Guardar cambios'}
              </Button>
            </div>
          </div>

          {/* Vista previa del encabezado tal como saldrá impreso */}
          <aside className="border border-regla bg-white p-4 relative">
            <div className="franja-carbon absolute inset-x-0 -top-px h-1" aria-hidden />
            <h3 className="font-display font-bold text-[15px] mb-3">
              Así se verá en la factura
            </h3>
            <div className="text-center pb-3 border-b border-regla">
              <div className="flex justify-center mb-2">
                <Logo
                  url={logoNuevo ? URL.createObjectURL(logoNuevo)
                       : (quitarLogo ? null : datos.logoUrl)}
                  nombre={datos.nombre ?? ''}
                  tamano={56}
                />
              </div>
              <p className="font-display font-bold text-base">{datos.nombre}</p>
              <p className="text-[11.5px] text-tinta-suave mt-0.5">
                {datos.direccion}
                {datos.nit && ` · NIT ${datos.nit}`}
                {datos.telefono && ` · Tel. ${datos.telefono}`}
              </p>
            </div>
            <p className="text-tinta-suave text-[11.5px] mt-3">
              El logotipo también aparecerá junto al nombre del negocio en la
              página pública de la plataforma.
            </p>
          </aside>
        </form>
      )}
    </>
  )
}
