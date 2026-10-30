import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

export default defineConfig({
  plugins: [react(), tailwindcss()],
  server: {
    port: 5173,
    // El backend corre en 8080. Con este proxy, el frontend llama a /api
    // y Vite lo redirige, evitando problemas de CORS en desarrollo.
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
      // Los logotipos y fotos de productos tambien los sirve el backend.
      // Sin esta linea el navegador los buscaria en el puerto del frontend
      // y las imagenes saldrian rotas.
      '/archivos': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
})
