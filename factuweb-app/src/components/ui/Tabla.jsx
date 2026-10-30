/** Tabla con el estilo de libro contable: encabezados discretos, cifras a la derecha. */

export function Tabla({ columnas, children }) {
  return (
    <div className="overflow-x-auto">
      <table className="w-full text-[13px] border-collapse">
        <thead>
          <tr className="border-b-[1.5px] border-tinta">
            {columnas.map((c, i) => (
              <th key={i}
                  className={`py-2 px-2.5 text-[10.5px] tracking-wider uppercase
                              text-tinta-suave font-semibold
                              ${c.derecha ? 'text-right' : 'text-left'}`}>
                {c.titulo}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>{children}</tbody>
      </table>
    </div>
  )
}

export function Fila({ atenuada = false, children }) {
  return (
    <tr className={`border-b border-regla hover:bg-papel ${atenuada ? 'opacity-55' : ''}`}>
      {children}
    </tr>
  )
}

export function Celda({ derecha = false, mono = false, suave = false, className = '', children }) {
  return (
    <td className={`py-2.5 px-2.5 ${derecha ? 'text-right' : ''} ${mono ? 'num' : ''}
                    ${suave ? 'text-tinta-suave' : ''} ${className}`}>
      {children}
    </td>
  )
}
