/** Funciones de presentacion compartidas por las pantallas*/

export const dinero = (valor) => {
  const n = Number(valor ?? 0)
  return '$' + n.toLocaleString('en-US', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  })
}

export const fecha = (iso) => {
  if (!iso) return ''
  const d = new Date(iso)
  return d.toLocaleDateString('es-SV', {
    day: '2-digit', month: '2-digit', year: 'numeric',
  })
}

export const fechaHora = (iso) => {
  if (!iso) return ''
  const d = new Date(iso)
  return d.toLocaleString('es-SV', {
    day: '2-digit', month: '2-digit', year: 'numeric',
    hour: '2-digit', minute: '2-digit',
  })
}

/** Fecha de hoy en formato ISO, para los campos de tipo date*/
export const hoyISO = () => new Date().toISOString().slice(0, 10)

/** Primer dia del mes actual en formato ISO*/
export const inicioMesISO = () => {
  const d = new Date()
  return new Date(d.getFullYear(), d.getMonth(), 1).toISOString().slice(0, 10)
}

/** Iniciales de un negocio, para el distintivo de la pagina publica */
export const iniciales = (nombre = '') => {
  const partes = nombre.trim().split(/\s+/).filter((p) => p.length > 2)
  return partes.slice(0, 2).map((p) => p[0].toUpperCase()).join('') || '??'
}
