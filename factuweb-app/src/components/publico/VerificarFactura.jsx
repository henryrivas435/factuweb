import { useEffect, useState } from 'react'
import { useParams, Link } from 'react-router-dom'
import { publicoService } from '../../services/publicoService'
import { mensajeError } from '../../utils/alertasApi'
import { Marca } from '../ui/Marca'
import HojaFactura from '../facturas/HojaFactura'
import { Alert, Loading } from '../ui/UI'

/**
 * Pantalla publica a la que lleva el codigo QR de una factura.
 * Permite a cualquiera comprobar que el documento es autentico.
 */
export default function VerificarFactura() {
  const { numero } = useParams()
  const [factura, setFactura] = useState(null)
  const [error, setError] = useState('')

  useEffect(() => {
    publicoService.verificarFactura(numero)
      .then(({ data }) => setFactura(data))
      .catch((e) => setError(mensajeError(e)))
  }, [numero])

  return (
    <div className="min-h-screen p-5">
      <div className="max-w-[620px] mx-auto">
        <div className="text-center mb-5">
          <Link to="/"><Marca tamano="grande" /></Link>
          <p className="text-tinta-suave text-[12.5px] mt-1">Verificación de factura</p>
        </div>

        <Alert>{error}</Alert>
        {!factura && !error && <Loading texto="Buscando la factura…" />}

        {factura && (
          <>
            <div className="border-l-[3px] border-libro bg-[#E7F4F0] text-libro px-3 py-2
                            text-[13px] mb-3">
              Documento auténtico, emitido por {factura.empresaNombre}.
            </div>
            <HojaFactura factura={factura} />
          </>
        )}
      </div>
    </div>
  )
}
