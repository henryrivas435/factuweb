/** Pie de las pantallas internas. No se imprime en las facturas. */
export default function Footer() {
  return (
    <footer className="no-imprimir mt-8 pt-3 border-t border-regla
                       text-[12px] text-tinta-suave">
      © {new Date().getFullYear()} FactuWeb — Sistema de facturación
    </footer>
  )
}
