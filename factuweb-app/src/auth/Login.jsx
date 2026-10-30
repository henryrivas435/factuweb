import { useState } from 'react'
import { Link, useNavigate, useLocation } from 'react-router-dom'
import { useAuth } from './AuthContext'
import { mensajeError } from '../utils/alertasApi'
import { Marca } from '../components/ui/Marca'
import { Alert, Button, Field, Input } from '../components/ui/UI'

export default function Login() {
  const { iniciarSesion } = useAuth()
  const navegar = useNavigate()
  const ubicacion = useLocation()

  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setIsSubmitting(true)
    try {
      const datos = await iniciarSesion(email, password)
      const destino = ubicacion.state?.desde
        || (datos.rol === 'ADMINISTRADOR' ? '/panel' : '/facturas/nueva')
      navegar(destino, { replace: true })
    } catch (err) {
      setError(mensajeError(err))
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center p-5">
      <div className="w-full max-w-[400px] bg-white border border-regla shadow-sm px-7 py-7.5 relative">
        <div className="franja-carbon absolute inset-x-0 top-0 h-1" aria-hidden />

        <div className="text-center">
          <Marca tamano="grande" />
          <p className="text-tinta-suave text-[12.5px] mt-1 mb-6">
            Entre a la cuenta de su negocio
          </p>
        </div>

        <Alert>{error}</Alert>

        <form onSubmit={handleSubmit}>
          <Field label="Correo">
            <Input type="email" value={email} required autoFocus
                     onChange={(e) => setEmail(e.target.value)}
                     placeholder="correo@negocio.sv" />
          </Field>
          <Field label="Contraseña">
            <Input type="password" value={password} required
                     onChange={(e) => setPassword(e.target.value)}
                     placeholder="••••••••" />
          </Field>
          <Button type="submit" disabled={isSubmitting} style={{ width: '100%' }}>
            {isSubmitting ? 'Entrando…' : 'Entrar'}
          </Button>
        </form>

        <p className="text-center text-[12px] text-tinta-suave mt-4 pt-3.5 border-t border-regla">
          ¿Su negocio aún no está registrado?<br />
          <Link to="/registro" className="text-tinta font-semibold hover:underline">
            Registrarlo ahora
          </Link>
        </p>
      </div>
    </div>
  )
}
