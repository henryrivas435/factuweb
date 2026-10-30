import { useEffect, useState } from 'react'
import { useParams, useNavigate, useLocation, Link } from 'react-router-dom'
import { facturaService as apiFacturas } from '../../services/facturaService'
import { mensajeError } from '../../utils/alertasApi'
import { useAuth } from '../../auth/AuthContext'
import { Encabezado } from '../ui/Encabezado'
import { Button, Alert, Loading } from '../ui/UI'
import HojaFactura from './HojaFactura'

export default function DetalleFactura() {
  const { id } = useParams()
  const navegar = useNavigate()
  const ubicacion = useLocation()
  const { esAdministrador } = useAuth()

  const [factura, setFactura] = useState(null)
  const [error, setError] = useState('')
  const [aviso, setAlert] = useState(
    ubicacion.state?.recienEmitida ? 'Factura emitida correctamente. El stock ya fue descontado.' : ''
  )
  const [anulando, setAnulando] = useState(false)

  const cargar = () => {
    apiFacturas.obtener(id)
      .then(({ data }) => setFactura(data))
      .catch((e) => setError(mensajeError(e)))
  }

  useEffect(() => { cargar() }, [id])

  const anular = async () => {
    if (!confirm('¿Anular esta factura? El stock volverá al inventario y la factura conservará su número.')) return
    setAnulando(true); setError(''); setAlert('')
    try {
      const { data } = await apiFacturas.anular(id)
      setFactura(data)
      setAlert('Factura anulada. El stock fue devuelto al inventario.')
    } catch (e) {
      setError(mensajeError(e))
    } finally {
      setAnulando(false)
    }
  }

  return (
    <>
      <Encabezado
        titulo={factura ? `Factura N.º ${factura.numeroFactura}` : 'Factura'}
        subtitulo={factura?.empresaNombre}
      >
        <Button variant="fantasma" onClick={() => navegar('/facturas')}>Volver</Button>
        <Button variant="fantasma" onClick={() => window.print()}>Imprimir</Button>
        {esAdministrador && factura?.estado === 'EMITIDA' && (
          <Button variant="peligro" onClick={anular} disabled={anulando}>
            {anulando ? 'Anulando…' : 'Anular'}
          </Button>
        )}
      </Encabezado>

      <div className="no-imprimir">
        <Alert>{error}</Alert>
        <Alert type="exito">{aviso}</Alert>
      </div>

      {!factura && !error ? <Loading /> : factura && (
        <div className="marco-impresion max-w-[620px]">
          <HojaFactura factura={factura} />
          <p className="no-imprimir text-tinta-suave text-xs mt-3">
            Los datos del cliente y de cada producto son los que estaban vigentes al
            momento de la venta, aunque después se hayan modificado.{' '}
            <Link to="/facturas" className="underline">Volver al historial</Link>
          </p>
        </div>
      )}
    </>
  )
}
