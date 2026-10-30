// src/services/archivoService.js
import client from './axiosClient'

/* Carga de imagenes (logotipos y fotos de productos). */
export const archivoService = {
  /** Sube el logotipo del negocio y devuelve la ruta donde quedo guardado. */
  subirLogo: (archivo) => {
    const cuerpo = new FormData()
    cuerpo.append('archivo', archivo)
    return client.post('/archivos/logo', cuerpo, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })
  },

  /** Sube la foto de un producto. */
  subirImagenProducto: (archivo) => {
    const cuerpo = new FormData()
    cuerpo.append('archivo', archivo)
    return client.post('/archivos/producto', cuerpo, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })
  },
}
