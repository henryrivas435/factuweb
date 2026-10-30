# FactuWeb API

API REST del sistema de facturación multiempresa **FactuWeb**, desarrollada con
Spring Boot, Spring Security (JWT) y PostgreSQL.

Módulo: Construcción de Aplicaciones Web en JSP · Ciclo 02-2026

---

## Qué hace esta API

FactuWeb es una plataforma donde varios negocios se registran y emiten sus
facturas. Todos comparten la misma base de datos, pero cada uno solo puede ver
y modificar su propia información. Esa separación se logra con la llave foránea
`id_empresa`, presente en casi todas las tablas, y con el `idEmpresa` que viaja
dentro del token JWT de cada usuario.

---

## Requisitos

| Herramienta | Versión |
|---|---|
| JDK | 17 o superior |
| PostgreSQL | 14 o superior |
| IntelliJ IDEA | Community o Ultimate |
| Maven | Incluido en IntelliJ, no hace falta instalarlo aparte |

---

## Cómo abrirlo en IntelliJ IDEA

1. Descomprimir `factuweb-api.zip`.
2. En IntelliJ: **File → Open** y seleccionar la carpeta `factuweb-api`
   (la que contiene el `pom.xml`).
3. IntelliJ detecta que es un proyecto Maven y descarga las dependencias solo.
   La primera vez tarda unos minutos.
4. Si no lo hace, abrir el panel **Maven** (barra derecha) y presionar el botón
   de recargar.
5. Verificar en **File → Project Structure → Project** que el SDK sea Java 17 o superior.

---

## Preparar la base de datos

Crear la base en PostgreSQL:

```sql
CREATE DATABASE factuweb;
```

Si el usuario o la contraseña de PostgreSQL no son `postgres/postgres`,
ajustarlos en `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/factuweb
    username: postgres
    password: postgres
```

Las tablas se crean solas al arrancar (`ddl-auto: update`) y los datos de
prueba se cargan desde `data.sql`.

---

## Ejecutar

En IntelliJ, abrir `FactuwebApiApplication.java` y presionar el botón verde de
ejecutar. La API queda disponible en `http://localhost:8080`.

---

## Cuentas de prueba

La contraseña de todas es **`password123`**.

| Correo | Rol | Negocio |
|---|---|---|
| admin@elprogreso.sv | Administrador | Librería El Progreso |
| ana@elprogreso.sv | Vendedor | Librería El Progreso |
| luis@elprogreso.sv | Vendedor | Librería El Progreso |
| admin@launion.sv | Administrador | Ferretería La Unión |
| admin@sanrafael.sv | Administrador | Farmacia San Rafael |
| admin@cuscatlan.sv | Administrador | Minisúper Cuscatlán |
| admin@elmotorista.sv | Administrador | Repuestos El Motorista |
| admin@donatere.sv | Administrador | Panadería Doña Tere |
| admin@elcampo.sv | Administrador | Agroveterinaria El Campo |

> Para comprobar la separación de datos: inicien sesión con `admin@elprogreso.sv`
> y luego con `admin@launion.sv`. Cada uno verá un stock completamente distinto.

---

## Endpoints

### Públicos (sin token)

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/auth/login` | Iniciar sesión, devuelve el token |
| POST | `/api/auth/registro` | Registrar un negocio nuevo con su administrador |
| GET | `/api/publico/negocios` | Lista de negocios de la página pública |
| GET | `/api/facturas/verificar/{numero}` | Verificación de factura (destino del QR) |

### Con token (encabezado `Authorization: Bearer <token>`)

| Método | Ruta | Rol | Descripción |
|---|---|---|---|
| GET | `/api/auth/yo` | Ambos | Datos de la sesión actual |
| GET | `/api/empresa` | Ambos | Datos del propio negocio |
| PUT | `/api/empresa` | Admin | Actualizar datos del negocio |
| GET | `/api/productos` | Ambos | Listar stock |
| GET | `/api/productos/buscar?texto=` | Ambos | Buscar por nombre o item |
| GET | `/api/productos/bajo-minimo` | Ambos | Productos que llegaron al mínimo |
| POST | `/api/productos` | Admin | Crear producto |
| PUT | `/api/productos/{id}` | Admin | Editar producto |
| DELETE | `/api/productos/{id}` | Admin | Baja lógica (marca inactivo) |
| PATCH | `/api/productos/{id}/reactivar` | Admin | Reactivar producto |
| GET | `/api/clientes` | Ambos | Listar clientes |
| POST | `/api/clientes` | Ambos | Crear cliente |
| PUT | `/api/clientes/{id}` | Ambos | Editar cliente |
| DELETE | `/api/clientes/{id}` | Admin | Eliminar cliente |
| GET | `/api/categorias` | Ambos | Listar categorías |
| POST | `/api/categorias` | Admin | Crear categoría |
| GET | `/api/usuarios` | Admin | Listar vendedores |
| POST | `/api/usuarios` | Admin | Crear vendedor |
| PUT | `/api/usuarios/{id}` | Admin | Editar vendedor |
| DELETE | `/api/usuarios/{id}` | Admin | Desactivar cuenta |
| POST | `/api/facturas` | Ambos | **Emitir factura (transacción)** |
| GET | `/api/facturas` | Ambos | Historial de facturas |
| GET | `/api/facturas/periodo?desde=&hasta=&estado=` | Ambos | Filtrar por fechas |
| GET | `/api/facturas/{id}` | Ambos | Ver una factura con su detalle |
| PATCH | `/api/facturas/{id}/anular` | Admin | Anular (no borra, devuelve stock) |
| GET | `/api/panel` | Admin | Indicadores del panel |
| GET | `/api/reportes?tipo=&desde=&hasta=` | Admin | Generar reporte |
| GET | `/api/reportes/historial` | Admin | Reportes generados |

---

## Probar con Postman o Thunder Client

**1. Iniciar sesión**

```
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "ana@elprogreso.sv",
  "password": "password123"
}
```

Copiar el `token` de la respuesta.

**2. Emitir una factura**

```
POST http://localhost:8080/api/facturas
Authorization: Bearer <token>
Content-Type: application/json

{
  "idCliente": 1,
  "lineas": [
    { "idProducto": 1, "cantidad": 2 },
    { "idProducto": 2, "cantidad": 5 }
  ]
}
```

La respuesta trae la factura completa con su número correlativo, el detalle,
el IVA calculado y la URL del QR. Al consultar `/api/productos` se verá que el
stock ya se descontó.

**3. Comprobar que la transacción funciona**

Pedir una cantidad mayor al stock disponible:

```json
{ "idCliente": 1, "lineas": [ { "idProducto": 4, "cantidad": 9999 } ] }
```

Devuelve error 400 y **no** se crea la factura ni se modifica ningún stock:
la transacción se revierte completa.

---

## Estructura del proyecto

```
src/main/java/sv/com/factuweb/api/
├── FactuwebApiApplication.java   Clase principal
├── config/                       Configuración de seguridad y CORS
├── security/                     JWT: generación, filtro y usuario autenticado
├── entity/                       Las 8 entidades del DER
├── repository/                   Consultas con Spring Data JPA
├── service/                      Lógica de negocio (incluye la transacción)
├── controller/                   Endpoints REST
├── dto/                          Objetos de transferencia de datos
│   └── auth/                     DTOs de inicio de sesion y registro
└── exception/                    Manejo uniforme de errores
```

---

## Decisiones de diseño implementadas

**Separación por negocio.** Todos los servicios reciben el `idEmpresa` del
usuario autenticado y filtran por él. Un negocio no puede leer ni modificar
datos de otro, aunque conozca el id del registro.

**Copia de datos al facturar.** `FACTURA` guarda el nombre, DUI y dirección del
cliente, y `DETALLE_FACTURA` guarda el item, nombre, descripción y precio del
producto. Si el negocio cambia un precio después, las facturas antiguas
conservan el precio real que se cobró.

**Estado en lugar de borrado.** Las facturas nunca se eliminan: cambian a
`ANULADA` o `ELIMINADA`. Así la numeración correlativa no queda con saltos sin
explicación. Al anular, el stock se devuelve al inventario.

**Borrado lógico.** `EMPRESA`, `USUARIO` y `PRODUCTO` tienen el campo `activo`.
En lugar de un `DELETE` se marca como inactivo, evitando registros huérfanos.

**Transacción de facturación.** El método `emitir()` de `FacturaService` está
anotado con `@Transactional`: calcula el correlativo, copia los datos, calcula
los totales y descuenta el stock. Si algo falla, se revierte todo.

---

## Conectar el frontend de React

CORS ya está configurado para `http://localhost:5173` (Vite) y
`http://localhost:3000` (Create React App). Si usan otro puerto, agregarlo en
`config/SecurityConfig.java`, en el método `corsConfigurationSource()`.


---

## Convención de nombres de los DTO

Todas las clases del paquete `dto` llevan el sufijo **`DTO`** y toman el nombre
de la entidad o del concepto que transportan: `ClienteDTO`, `ProductoDTO`,
`FacturaDTO`, `UsuarioDTO`, etc.

Todos son **clases** con atributos privados, constructor vacío, constructor
completo, y sus respectivos *getters* y *setters*. El constructor vacío lo
necesita Jackson para convertir el JSON entrante en objeto.

Cuando una entidad necesita entrada y salida, se usa **un solo DTO** con todos
los campos: los de entrada se envían al crear y los de salida se llenan al
consultar. Por ejemplo, `UsuarioDTO` recibe `password` al crear la cuenta, pero
al consultar ese campo vuelve en `null` porque la contraseña nunca se devuelve.

El subpaquete `dto/auth` agrupa lo relacionado con la sesión:
`LoginRequestDTO`, `LoginResponseDTO` y `RegistroNegocioDTO`.
