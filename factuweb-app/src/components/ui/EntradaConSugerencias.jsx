import { useId } from 'react'
import { Input } from './UI'

/**
 * Campo de texto libre que ademas ofrece sugerencias.
 *
 * Se usa para el rubro del negocio: la plataforma acepta cualquier giro
 * comercial, asi que no puede limitarse a una lista cerrada. Las opciones
 * solo estan para ahorrar escritura en los casos mas comunes.
 */
export default function EntradaConSugerencias({ sugerencias = [], ...props }) {
  const idLista = useId()

  return (
    <>
      <Input list={idLista} autoComplete="off" {...props} />
      <datalist id={idLista}>
        {sugerencias.map((s) => <option key={s} value={s} />)}
      </datalist>
    </>
  )
}
