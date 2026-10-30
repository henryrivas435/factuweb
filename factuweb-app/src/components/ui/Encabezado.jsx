/** Encabezado de cada pantalla: titulo, subtitulo y acciones. */
export function Encabezado({ titulo, subtitulo, children }) {
  return (
    <header className="no-imprimir flex justify-between items-end gap-4 flex-wrap
                       pb-3.5 border-b border-regla mb-4.5">
      <div>
        <h1 className="font-display font-bold text-[23px] tracking-tight">{titulo}</h1>
        {subtitulo && <p className="text-tinta-suave text-[13px] mt-0.5">{subtitulo}</p>}
      </div>
      {children && <div className="flex gap-2">{children}</div>}
    </header>
  )
}

export default Encabezado
