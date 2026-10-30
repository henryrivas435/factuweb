import { dinero, fechaHora } from '../../utils/formato'
import Logo from '../ui/Logo'
import { QRCodeSVG } from 'qrcode.react'

/**
 * La hoja de factura tal como se imprimira.
 *
 * El diseno viene del talonario en papel: franja de carbon arriba, sello
 * de goma con el estado, linea de perforacion y el codigo de verificacion
 * al pie.
 */
export default function HojaFactura({ factura, empresa, previa = false }) {
  if (!factura) return null

  const lineas = factura.detalles ?? []

  /*
   * El QR apunta a la pantalla publica de verificacion de esta misma
   * aplicacion. Se arma con la direccion desde la que se abrio, para que
   * al escanear desde un telefono en la misma red el enlace funcione.
   * Si la factura todavia no se ha emitido, no hay numero y no se dibuja.
   */
  const enlaceVerificacion = factura.numeroFactura
    ? `${window.location.origin}/verificar/${factura.numeroFactura}`
    : null
  const estado = factura.estado ?? 'EMITIDA'
  const colorBadge =
    estado === 'EMITIDA' ? 'var(--color-libro)'
    : estado === 'ANULADA' ? 'var(--color-sello)'
    : 'var(--color-tinta-suave)'

  return (
    <article className="hoja-impresion relative bg-white border border-regla shadow-sm px-6 py-5.5">
      <div className="franja-carbon absolute inset-x-0 -top-px h-1" aria-hidden />

      <header className="text-center pb-3 border-b border-regla">
        {(empresa?.logoUrl ?? factura.empresaLogoUrl) && (
          <div className="flex justify-center mb-2">
            <Logo url={empresa?.logoUrl ?? factura.empresaLogoUrl}
                  nombre={empresa?.nombre ?? factura.empresaNombre ?? ''}
                  tamano={52} />
          </div>
        )}
        <h3 className="font-display font-bold text-base">
          {empresa?.nombre ?? factura.empresaNombre}
        </h3>
        <p className="text-[11.5px] text-tinta-suave mt-0.5">
          {empresa?.direccion ?? factura.empresaDireccion}
          {(empresa?.nit ?? factura.empresaNit) && ` · NIT ${empresa?.nit ?? factura.empresaNit}`}
          {(empresa?.telefono ?? factura.empresaTelefono) && ` · Tel. ${empresa?.telefono ?? factura.empresaTelefono}`}
        </p>
      </header>

      <div className="flex justify-between gap-3.5 py-3 text-[12.5px]">
        <div>
          <dt className="text-[10px] tracking-wider uppercase text-tinta-suave">
            Factura de consumidor final
          </dt>
          <dd className="num text-base font-medium">
            N.º {factura.numeroFactura ?? '——————'}
          </dd>
        </div>
        <div className="text-right">
          <dt className="text-[10px] tracking-wider uppercase text-tinta-suave">Fecha</dt>
          <dd className="num">
            {factura.fechaEmision ? fechaHora(factura.fechaEmision) : 'Al emitir'}
          </dd>
        </div>
      </div>

      <div className="text-[12.5px] pb-2.5">
        <div>
          <span className="text-tinta-suave">Cliente:</span>{' '}
          {factura.clienteNombre || 'Consumidor final'}
        </div>
        {(factura.clienteDui || factura.vendedor) && (
          <div>
            {factura.clienteDui && (
              <>
                <span className="text-tinta-suave">DUI:</span>{' '}
                <span className="num">{factura.clienteDui}</span>
              </>
            )}
            {factura.vendedor && (
              <>
                {factura.clienteDui && ' · '}
                <span className="text-tinta-suave">Atendió:</span> {factura.vendedor}
              </>
            )}
          </div>
        )}
      </div>

      <table className="w-full text-[13px]">
        <thead>
          <tr className="border-b-[1.5px] border-tinta text-tinta-suave">
            <th className="py-1.5 pr-3 text-right text-[10.5px] tracking-wider uppercase font-semibold w-[14%]">Cant.</th>
            <th className="py-1.5 text-left text-[10.5px] tracking-wider uppercase font-semibold">Producto</th>
            <th className="py-1.5 text-right text-[10.5px] tracking-wider uppercase font-semibold">Precio</th>
            <th className="py-1.5 text-right text-[10.5px] tracking-wider uppercase font-semibold">Total</th>
          </tr>
        </thead>
        <tbody>
          {lineas.length === 0 ? (
            <tr>
              <td colSpan={4} className="py-6 text-center text-tinta-suave text-[12.5px]">
                Agregue productos para verlos aquí
              </td>
            </tr>
          ) : lineas.map((d, i) => (
            <tr key={d.idDetalle ?? i} className="border-b border-regla last:border-0">
              <td className="py-1.5 pr-3 num text-right">{d.cantidad}</td>
              <td className="py-1.5">{d.nombre}</td>
              <td className="py-1.5 num text-right">{dinero(d.precio)}</td>
              <td className="py-1.5 num text-right">{dinero(d.subtotalLinea)}</td>
            </tr>
          ))}
        </tbody>
      </table>

      <div className="ml-auto w-[230px] text-[13px] mt-3">
        <div className="flex justify-between py-0.5">
          <span className="text-tinta-suave">Subtotal</span>
          <span className="num">{dinero(factura.subtotal)}</span>
        </div>
        <div className="flex justify-between py-0.5">
          <span className="text-tinta-suave">IVA (13 %)</span>
          <span className="num">{dinero(factura.iva)}</span>
        </div>
        <div className="flex justify-between pt-2 mt-1 border-t-[1.5px] border-tinta
                        font-display font-bold text-[17px]">
          <span>Total</span>
          <span className="num">{dinero(factura.total)}</span>
        </div>
      </div>

      {!previa && factura.numeroFactura && (
        <div className="-rotate-6 border-[3px] border-double inline-block
                        font-display font-extrabold text-[17px] tracking-[0.14em] px-3.5 py-1
                        rounded-sm opacity-85 -mt-12 ml-1 relative z-10"
             style={{ borderColor: colorBadge, color: colorBadge }}>
          {estado}
        </div>
      )}

      <div className="perforacion -mx-6 my-4 relative">
        <span className="absolute left-1/2 -translate-x-1/2 -top-2 bg-white px-2.5
                         text-[9.5px] tracking-[0.14em] uppercase text-[#B8B3A6]">
          desprender por aquí
        </span>
      </div>

      <div className="flex gap-3.5 items-center">
        {enlaceVerificacion ? (
          <QRCodeSVG
            value={enlaceVerificacion}
            size={62}
            level="M"
            bgColor="#FFFFFF"
            fgColor="#14213D"
            title={`Verificar la factura ${factura.numeroFactura}`}
            className="shrink-0"
          />
        ) : (
          /* Todavia no hay numero de factura: se reserva el espacio */
          <div aria-hidden
               className="w-[62px] h-[62px] border border-dashed border-regla shrink-0" />
        )}
        <p className="text-[11.5px] text-tinta-suave">
          Escanee para verificar esta factura en línea.<br />
          <span className="num text-[11px] break-all">
            {enlaceVerificacion ?? 'Se genera al emitir la factura'}
          </span>
        </p>
      </div>
    </article>
  )
}
