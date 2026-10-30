import jsPDF from 'jspdf'
import autoTable from 'jspdf-autotable'

/*
 * Colores del sistema, para que el PDF se vea como el resto de FactuWeb.
 * jsPDF los necesita en RGB, no en hexadecimal.
 */
const TINTA = [20, 33, 61]
const CARBON = [242, 199, 68]
const SUAVE = [74, 85, 112]
const REGLA = [221, 217, 206]

const TITULOS = {
  ventas_por_periodo: 'Ventas por período',
  productos_mas_vendidos: 'Productos más vendidos',
  ventas_por_vendedor: 'Ventas por vendedor',
}

const money = (v) =>
  '$' + Number(v ?? 0).toLocaleString('en-US', {
    minimumFractionDigits: 2, maximumFractionDigits: 2,
  })

const dia = (iso) => {
  if (!iso) return ''
  const [a, m, d] = iso.split('-')
  return `${d}/${m}/${a}`
}

/**
 * Arma el reporte en PDF y lo descarga.
 *
 * Se genera en el navegador, sin pedirle nada al servidor: los datos ya
 * estan en pantalla, asi que no tiene sentido volver a consultarlos.
 *
 * @param datos    respuesta del endpoint de reportes
 * @param negocio  datos de la empresa, para el encabezado
 * @param periodo  { tipo, desde, hasta } tal como se pidio el reporte
 * @param usuario  nombre de quien lo genero
 */
export function descargarReportePDF({ datos, negocio, periodo, usuario }) {
  const doc = new jsPDF({ unit: 'pt', format: 'letter' })
  const ancho = doc.internal.pageSize.getWidth()
  const margen = 48
  let y = 52

  /* Franja de papel carbon, como en las facturas */
  doc.setFillColor(...CARBON)
  for (let x = margen; x < ancho - margen; x += 14) {
    doc.rect(x, 28, 7, 4, 'F')
  }

  /* Encabezado  */
  doc.setTextColor(...TINTA)
  doc.setFont('helvetica', 'bold')
  doc.setFontSize(17)
  doc.text(negocio?.nombre ?? 'FactuWeb', margen, y)

  y += 15
  doc.setFont('helvetica', 'normal')
  doc.setFontSize(9)
  doc.setTextColor(...SUAVE)
  const señas = [negocio?.direccion, negocio?.nit && `NIT ${negocio.nit}`,
                 negocio?.telefono && `Tel. ${negocio.telefono}`]
                .filter(Boolean).join('  ·  ')
  if (señas) doc.text(señas, margen, y)

  /* Titulo del reporte*/
  y += 30
  doc.setDrawColor(...TINTA)
  doc.setLineWidth(1)
  doc.line(margen, y, ancho - margen, y)

  y += 20
  doc.setFont('helvetica', 'bold')
  doc.setFontSize(13)
  doc.setTextColor(...TINTA)
  doc.text(TITULOS[periodo.tipo] ?? 'Reporte de ventas', margen, y)

  y += 15
  doc.setFont('helvetica', 'normal')
  doc.setFontSize(9)
  doc.setTextColor(...SUAVE)
  doc.text(`Del ${dia(periodo.desde)} al ${dia(periodo.hasta)}`, margen, y)

  /* Indicadores*/
  y += 26
  const indicadores = [
    ['Total facturado', money(datos.totalFacturado), `${datos.facturasEmitidas ?? 0} facturas`],
    ['IVA recaudado', money(datos.ivaRecaudado), '13 % sobre subtotal'],
    ['Unidades vendidas', String(datos.unidadesVendidas ?? 0),
     `${datos.productosMasVendidos?.length ?? 0} productos distintos`],
  ]
  const anchoCaja = (ancho - margen * 2 - 20) / 3

  indicadores.forEach(([etiqueta, valor, nota], i) => {
    const x = margen + i * (anchoCaja + 10)
    doc.setDrawColor(...REGLA)
    doc.setLineWidth(0.7)
    doc.rect(x, y, anchoCaja, 54)
    doc.setFillColor(...TINTA)
    doc.rect(x, y, anchoCaja, 2.5, 'F')

    doc.setFont('helvetica', 'normal')
    doc.setFontSize(7.5)
    doc.setTextColor(...SUAVE)
    doc.text(etiqueta.toUpperCase(), x + 10, y + 17)

    doc.setFont('courier', 'bold')
    doc.setFontSize(15)
    doc.setTextColor(...TINTA)
    doc.text(valor, x + 10, y + 36)

    doc.setFont('helvetica', 'normal')
    doc.setFontSize(7.5)
    doc.setTextColor(...SUAVE)
    doc.text(nota, x + 10, y + 48)
  })

  y += 76

  /* Estilo comun de las tablas*/
  const estilo = {
    theme: 'plain',
    styles: { font: 'helvetica', fontSize: 9, cellPadding: 5, textColor: TINTA },
    headStyles: {
      fontStyle: 'bold', fontSize: 7.5, textColor: SUAVE,
      lineWidth: { bottom: 1 }, lineColor: TINTA,
    },
    bodyStyles: { lineWidth: { bottom: 0.4 }, lineColor: REGLA },
    margin: { left: margen, right: margen },
  }

  /*Productos mas vendidos*/
  if (datos.productosMasVendidos?.length) {
    doc.setFont('helvetica', 'bold')
    doc.setFontSize(11)
    doc.setTextColor(...TINTA)
    doc.text('Productos más vendidos', margen, y)
    y += 8

    autoTable(doc, {
      ...estilo,
      startY: y,
      head: [['Item', 'Producto', 'Unidades', 'Ingresos']],
      body: datos.productosMasVendidos.map((p) => [
        p.item ?? '', p.nombre, String(p.unidades), money(p.ingresos),
      ]),
      columnStyles: {
        0: { font: 'courier', cellWidth: 70 },
        2: { halign: 'right', font: 'courier', cellWidth: 70 },
        3: { halign: 'right', font: 'courier', cellWidth: 85 },
      },
    })
    y = doc.lastAutoTable.finalY + 28
  }

  /*Ventas por vendedor*/
  if (datos.ventasPorVendedor?.length) {
    if (y > doc.internal.pageSize.getHeight() - 140) {
      doc.addPage()
      y = 52
    }
    doc.setFont('helvetica', 'bold')
    doc.setFontSize(11)
    doc.setTextColor(...TINTA)
    doc.text('Ventas por vendedor', margen, y)
    y += 8

    autoTable(doc, {
      ...estilo,
      startY: y,
      head: [['Vendedor', 'Facturas', 'Total']],
      body: datos.ventasPorVendedor.map((v) => [
        v.vendedor, String(v.facturas), money(v.total),
      ]),
      columnStyles: {
        1: { halign: 'right', font: 'courier', cellWidth: 80 },
        2: { halign: 'right', font: 'courier', cellWidth: 95 },
      },
    })
  }

  /* Pie en todas las paginas*/
  const total = doc.internal.getNumberOfPages()
  const alto = doc.internal.pageSize.getHeight()
  const generado = new Date().toLocaleString('es-SV', {
    day: '2-digit', month: '2-digit', year: 'numeric',
    hour: '2-digit', minute: '2-digit',
  })

  for (let i = 1; i <= total; i++) {
    doc.setPage(i)
    doc.setDrawColor(...REGLA)
    doc.setLineWidth(0.7)
    doc.line(margen, alto - 44, ancho - margen, alto - 44)

    doc.setFont('helvetica', 'normal')
    doc.setFontSize(7.5)
    doc.setTextColor(...SUAVE)
    doc.text(`Generado el ${generado}${usuario ? ' por ' + usuario : ''}`, margen, alto - 30)
    doc.text(`FactuWeb  ·  Página ${i} de ${total}`, ancho - margen, alto - 30, { align: 'right' })
  }

  /*Nombre del archivo*/
  const limpio = (negocio?.nombre ?? 'factuweb')
    .toLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g, '')
    .replace(/[^a-z0-9]+/g, '-').replace(/^-|-$/g, '')

  doc.save(`reporte-${limpio}-${periodo.desde}_a_${periodo.hasta}.pdf`)
}
