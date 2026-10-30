/** Marca de la plataforma. */
export function Marca({ tamano = 'normal' }) {
  const tamanos = { normal: 'text-[19px]', grande: 'text-[26px]' }
  return (
    <span className={`font-display font-extrabold tracking-tight ${tamanos[tamano]}`}>
      Factu<span className="text-carbon">Web</span>
    </span>
  )
}

export default Marca
