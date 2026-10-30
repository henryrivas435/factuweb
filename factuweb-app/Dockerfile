FROM node:20-alpine AS compilacion
WORKDIR /build

# Primero las dependencias, para aprovechar la cache de Docker
COPY package*.json ./
RUN npm install

# Luego el codigo y la construccion
COPY . .
RUN npm run build


# React se compila a archivos estaticos; no necesita Node para funcionar.
# Nginx los sirve y ademas redirige /api y /archivos hacia la API, igual
# que hace el proxy de Vite durante el desarrollo.
FROM nginx:alpine

COPY --from=compilacion /build/dist /usr/share/nginx/html
# Plantilla: nginx cambia ${API_URL} por la variable de entorno al arrancar
COPY templates/default.conf.template /etc/nginx/templates/default.conf.template
ENV API_URL=http://api:8080

EXPOSE 80
