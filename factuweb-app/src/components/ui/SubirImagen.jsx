import { useRef, useState } from 'react'
import { Button } from './UI'

/**
 * Select de imagen con vista previa.
 *
 * Muestra la imagen actual (o la recien elegida) y permite reemplazarla o
 * quitarla. La subida al servidor la hace la pantalla que lo usa, cuando el
 * usuario guarda el formulario.
 */
export default function SubirImagen({
  valor,           // ruta de la imagen ya guardada
  archivo,         // archivo recien elegido, aun sin subir
  onElegir,        // recibe el File elegido, o null si se quita
  etiqueta = 'Imagen',
  ayuda = 'JPG, PNG o WEBP. Máximo 2 MB.',
  ancho = 120,
  alto = 120,
}) {
  const entradaRef = useRef(null)
  const [error, setError] = useState('')

  /* Vista previa: si hay archivo nuevo se muestra ese; si no, el guardado. */
  const previa = archivo ? URL.createObjectURL(archivo) : valor

  const elegir = (e) => {
    const f = e.target.files?.[0]
    setError('')
    if (!f) return

    if (!f.type.startsWith('image/')) {
      setError('El archivo debe ser una imagen')
      return
    }
    if (f.size > 2 * 1024 * 1024) {
      setError('La imagen no debe pesar más de 2 MB')
      return
    }
    onElegir(f)
  }

  const quitar = () => {
    onElegir(null)
    setError('')
    if (entradaRef.current) entradaRef.current.value = ''
  }

  return (
    <div className="mb-3.5">
      <label className="block text-[11px] tracking-wider uppercase text-tinta-suave
                        font-semibold mb-1.5">
        {etiqueta}
      </label>

      <div className="flex gap-3.5 items-start flex-wrap">
        <div
          style={{ width: ancho, height: alto }}
          className="border border-regla bg-papel flex items-center justify-center
                     overflow-hidden shrink-0"
        >
          {previa ? (
            <img src={previa} alt="Vista previa"
                 className="w-full h-full object-contain" />
          ) : (
            <span className="text-tinta-suave text-[11px] text-center px-2">
              Sin imagen
            </span>
          )}
        </div>

        <div className="flex-1 min-w-[180px]">
          <input
            ref={entradaRef}
            type="file"
            accept="image/png,image/jpeg,image/webp,image/gif"
            onChange={elegir}
            className="block w-full text-[12.5px] text-tinta-suave
                       file:mr-2 file:px-3 file:py-1.5 file:rounded-sm
                       file:border file:border-tinta file:bg-tinta file:text-white
                       file:text-[12px] file:font-semibold file:cursor-pointer
                       hover:file:bg-[#0d1730]"
          />
          <p className="text-tinta-suave text-[11.5px] mt-1.5">{ayuda}</p>
          {error && <p className="text-sello text-xs mt-1">{error}</p>}

          {previa && (
            <Button type="button" variant="fantasma" size="chico"
                   onClick={quitar} style={{ marginTop: 8 }}>
              Quitar imagen
            </Button>
          )}
        </div>
      </div>
    </div>
  )
}
