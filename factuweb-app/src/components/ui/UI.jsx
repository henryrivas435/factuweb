/**
 * Piezas visuales compartidas.
 * Concentrarlas aqui evita repetir clases de Tailwind en cada pantalla
 * y mantiene el mismo lenguaje visual en todo el sistema.
 */

export function Button({ variant = 'solido', size = 'normal', children, ...props }) {
  const base =
    'font-semibold rounded-sm border transition-colors cursor-pointer ' +
    'disabled:opacity-50 disabled:cursor-not-allowed ' +
    'focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-carbon'

  const variants = {
    solido: 'bg-tinta border-tinta text-white hover:bg-[#0d1730]',
    fantasma: 'bg-transparent border-tinta text-tinta hover:bg-[#F0EEE7]',
    oro: 'bg-carbon border-carbon text-tinta hover:brightness-95',
    peligro: 'bg-transparent border-sello text-sello hover:bg-[#FBEAEA]',
  }
  const sizes = {
    chico: 'px-2.5 py-1 text-xs',
    normal: 'px-4 py-2 text-[13px]',
    grande: 'px-5 py-2.5 text-[14.5px]',
  }

  return (
    <button className={`${base} ${variants[variant]} ${sizes[size]}`} {...props}>
      {children}
    </button>
  )
}

export function Field({ label, error, children, className = '' }) {
  return (
    <div className={`mb-3.5 ${className}`}>
      {label && (
        <label className="block text-[11px] tracking-wider uppercase text-tinta-suave font-semibold mb-1.5">
          {label}
        </label>
      )}
      {children}
      {error && <p className="text-xs text-sello mt-1">{error}</p>}
    </div>
  )
}

export function Input(props) {
  return (
    <input
      className="w-full text-[13.5px] px-2.5 py-2 border border-regla rounded-sm bg-white
                 text-tinta focus:outline-none focus:border-tinta
                 focus:ring-3 focus:ring-tinta/10 disabled:bg-papel"
      {...props}
    />
  )
}

export function Select({ children, ...props }) {
  return (
    <select
      className="w-full text-[13.5px] px-2.5 py-2 border border-regla rounded-sm bg-white
                 text-tinta focus:outline-none focus:border-tinta focus:ring-3 focus:ring-tinta/10"
      {...props}
    >
      {children}
    </select>
  )
}

export function Card({ titulo, children, className = '' }) {
  return (
    <section className={`border border-regla bg-white p-4 ${className}`}>
      {titulo && (
        <h3 className="font-display font-bold text-[15px] mb-3">{titulo}</h3>
      )}
      {children}
    </section>
  )
}

/** Badge de goma: indica el estado de un documento o registro. */
export function Badge({ tipo = 'neutro', children }) {
  const tipos = {
    ok: 'text-libro border-libro',
    no: 'text-sello border-sello',
    neutro: 'text-tinta-suave border-regla',
  }
  return (
    <span
      className={`inline-block font-display font-bold text-[10px] tracking-[0.11em]
                  uppercase px-1.5 py-0.5 border-[1.5px] rounded-sm ${tipos[tipo]}`}
    >
      {children}
    </span>
  )
}

export function Indicator({ etiqueta, valor, nota, tono = 'normal' }) {
  const tonos = {
    normal: 'border-t-tinta',
    bueno: 'border-t-libro',
    alerta: 'border-t-sello',
  }
  return (
    <div className={`border border-regla border-t-[3px] ${tonos[tono]} bg-white p-3.5`}>
      <dt className="text-[10.5px] tracking-wider uppercase text-tinta-suave">{etiqueta}</dt>
      <dd className="num text-[25px] font-medium mt-1 tracking-tight">{valor}</dd>
      {nota && <p className="text-tinta-suave text-[11.5px]">{nota}</p>}
    </div>
  )
}

export function Loading({ texto = 'Loading…' }) {
  return (
    <p className="text-tinta-suave text-sm py-8 text-center" role="status">
      {texto}
    </p>
  )
}

export function Alert({ type = 'error', children }) {
  if (!children) return null
  const types = {
    error: 'border-sello bg-[#FBEAEA] text-sello',
    exito: 'border-libro bg-[#E7F4F0] text-libro',
    info: 'border-regla bg-papel text-tinta-suave',
  }
  return (
    <div className={`border-l-[3px] px-3 py-2 text-[13px] mb-3 ${types[type]}`} role="alert">
      {children}
    </div>
  )
}


export function Empty({ children }) {
  return (
    <p className="text-tinta-suave text-sm py-10 text-center border border-dashed border-regla">
      {children}
    </p>
  )
}
