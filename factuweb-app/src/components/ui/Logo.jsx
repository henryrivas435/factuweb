/**
 * Logotipo de un negocio.
 *
 * Si el negocio todavia no subio su logo, muestra sus iniciales sobre un
 * recuadro, para que la interfaz no quede con un hueco vacio.
 */
export default function Logo({ url, nombre = '', tamano = 40, className = '' }) {
  const iniciales = nombre
    .trim()
    .split(/\s+/)
    .filter((p) => p.length > 2)
    .slice(0, 2)
    .map((p) => p[0].toUpperCase())
    .join('') || '??'

  const estilo = { width: tamano, height: tamano }

  if (url) {
    return (
      <img
        src={url}
        alt={`Logotipo de ${nombre}`}
        style={estilo}
        className={`object-contain bg-white border border-regla shrink-0 ${className}`}
      />
    )
  }

  return (
    <div
      style={{ ...estilo, fontSize: Math.round(tamano * 0.38) }}
      aria-label={`${nombre} (sin logotipo)`}
      className={`bg-tinta text-carbon font-display font-extrabold shrink-0
                  flex items-center justify-center ${className}`}
    >
      {iniciales}
    </div>
  )
}
