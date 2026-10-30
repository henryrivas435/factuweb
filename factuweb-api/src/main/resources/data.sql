--  Datos de prueba de FactuWeb
INSERT INTO empresa (id_empresa, nombre, rubro, nit, direccion, telefono, fecha_registro, activo)
SELECT 1, 'Libreria y Papeleria El Progreso', 'Papeleria', '0614-000001-001-1',
       'Av. Principal, Aguilares', '2345-6789', CURRENT_TIMESTAMP, true
WHERE NOT EXISTS (SELECT 1 FROM empresa WHERE id_empresa = 1);

INSERT INTO empresa (id_empresa, nombre, rubro, nit, direccion, telefono, fecha_registro, activo)
SELECT 2, 'Ferreteria La Union', 'Ferreteria', '0614-000002-001-2',
       'Calle Nueva #7, Aguilares', '2345-9042', CURRENT_TIMESTAMP, true
WHERE NOT EXISTS (SELECT 1 FROM empresa WHERE id_empresa = 2);

INSERT INTO empresa (id_empresa, nombre, rubro, nit, direccion, telefono, fecha_registro, activo)
SELECT 3, 'Farmacia San Rafael', 'Farmacia', '0614-000003-001-3',
       'Barrio El Centro, El Paisnal', '2389-1122', CURRENT_TIMESTAMP, true
WHERE NOT EXISTS (SELECT 1 FROM empresa WHERE id_empresa = 3);

INSERT INTO empresa (id_empresa, nombre, rubro, nit, direccion, telefono, fecha_registro, activo)
SELECT 4, 'Minisuper Cuscatlan', 'Abarrotes', '0614-000004-001-4',
       'Col. Las Flores, Aguilares', '2345-3311', CURRENT_TIMESTAMP, true
WHERE NOT EXISTS (SELECT 1 FROM empresa WHERE id_empresa = 4);

INSERT INTO empresa (id_empresa, nombre, rubro, nit, direccion, telefono, fecha_registro, activo)
SELECT 5, 'Repuestos El Motorista', 'Repuestos', '0614-000005-001-5',
       'Carretera Troncal del Norte, Guazapa', '2374-5566', CURRENT_TIMESTAMP, true
WHERE NOT EXISTS (SELECT 1 FROM empresa WHERE id_empresa = 5);

INSERT INTO empresa (id_empresa, nombre, rubro, nit, direccion, telefono, fecha_registro, activo)
SELECT 6, 'Panaderia Dona Tere', 'Panaderia', '0614-000006-001-6',
       'Calle al Cementerio, Aguilares', '2345-7788', CURRENT_TIMESTAMP, true
WHERE NOT EXISTS (SELECT 1 FROM empresa WHERE id_empresa = 6);

INSERT INTO empresa (id_empresa, nombre, rubro, nit, direccion, telefono, fecha_registro, activo)
SELECT 7, 'Agroveterinaria El Campo', 'Agropecuaria', '0614-000007-001-7',
       'Km 38 Troncal del Norte, El Paisnal', '2389-4400', CURRENT_TIMESTAMP, true
WHERE NOT EXISTS (SELECT 1 FROM empresa WHERE id_empresa = 7);


INSERT INTO usuario (id_usuario, id_empresa, nombre, email, password, rol, activo)
SELECT 1, 1, 'Josue Martinez', 'admin@elprogreso.sv',
       '$2a$10$R5ZMXsaA2Z4tDdwyR/9QGumDYaN7UZ9bCKNUMYBSwAii/8LDw9y5S', 'ADMINISTRADOR', true
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE id_usuario = 1);

INSERT INTO usuario (id_usuario, id_empresa, nombre, email, password, rol, activo)
SELECT 2, 1, 'Ana Guzman', 'ana@elprogreso.sv',
       '$2a$10$R5ZMXsaA2Z4tDdwyR/9QGumDYaN7UZ9bCKNUMYBSwAii/8LDw9y5S', 'VENDEDOR', true
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE id_usuario = 2);

INSERT INTO usuario (id_usuario, id_empresa, nombre, email, password, rol, activo)
SELECT 3, 1, 'Luis Portillo', 'luis@elprogreso.sv',
       '$2a$10$R5ZMXsaA2Z4tDdwyR/9QGumDYaN7UZ9bCKNUMYBSwAii/8LDw9y5S', 'VENDEDOR', true
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE id_usuario = 3);


INSERT INTO usuario (id_usuario, id_empresa, nombre, email, password, rol, activo)
SELECT 4, 2, 'Mario Hernandez', 'admin@launion.sv',
       '$2a$10$R5ZMXsaA2Z4tDdwyR/9QGumDYaN7UZ9bCKNUMYBSwAii/8LDw9y5S', 'ADMINISTRADOR', true
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE id_usuario = 4);

INSERT INTO usuario (id_usuario, id_empresa, nombre, email, password, rol, activo)
SELECT 5, 3, 'Sofia Ramirez', 'admin@sanrafael.sv',
       '$2a$10$R5ZMXsaA2Z4tDdwyR/9QGumDYaN7UZ9bCKNUMYBSwAii/8LDw9y5S', 'ADMINISTRADOR', true
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE id_usuario = 5);

INSERT INTO usuario (id_usuario, id_empresa, nombre, email, password, rol, activo)
SELECT 6, 4, 'Carlos Mejia', 'admin@cuscatlan.sv',
       '$2a$10$R5ZMXsaA2Z4tDdwyR/9QGumDYaN7UZ9bCKNUMYBSwAii/8LDw9y5S', 'ADMINISTRADOR', true
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE id_usuario = 6);

INSERT INTO usuario (id_usuario, id_empresa, nombre, email, password, rol, activo)
SELECT 7, 5, 'Rene Vasquez', 'admin@elmotorista.sv',
       '$2a$10$R5ZMXsaA2Z4tDdwyR/9QGumDYaN7UZ9bCKNUMYBSwAii/8LDw9y5S', 'ADMINISTRADOR', true
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE id_usuario = 7);

INSERT INTO usuario (id_usuario, id_empresa, nombre, email, password, rol, activo)
SELECT 8, 6, 'Teresa Lopez', 'admin@donatere.sv',
       '$2a$10$R5ZMXsaA2Z4tDdwyR/9QGumDYaN7UZ9bCKNUMYBSwAii/8LDw9y5S', 'ADMINISTRADOR', true
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE id_usuario = 8);

INSERT INTO usuario (id_usuario, id_empresa, nombre, email, password, rol, activo)
SELECT 9, 7, 'Oscar Campos', 'admin@elcampo.sv',
       '$2a$10$R5ZMXsaA2Z4tDdwyR/9QGumDYaN7UZ9bCKNUMYBSwAii/8LDw9y5S', 'ADMINISTRADOR', true
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE id_usuario = 9);


INSERT INTO categoria (id_categoria, id_empresa, nombre, descripcion)
SELECT 1, 1, 'Papeleria', 'Articulos de papeleria en general'
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE id_categoria = 1);
INSERT INTO categoria (id_categoria, id_empresa, nombre, descripcion)
SELECT 2, 1, 'Escolar', 'Utiles escolares'
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE id_categoria = 2);
INSERT INTO categoria (id_categoria, id_empresa, nombre, descripcion)
SELECT 3, 1, 'Escritura', 'Lapiceros, marcadores y similares'
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE id_categoria = 3);
INSERT INTO categoria (id_categoria, id_empresa, nombre, descripcion)
SELECT 4, 1, 'Archivo', 'Folders, carpetas y archivadores'
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE id_categoria = 4);
INSERT INTO categoria (id_categoria, id_empresa, nombre, descripcion)
SELECT 5, 2, 'Herramientas', 'Herramientas manuales'
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE id_categoria = 5);
INSERT INTO categoria (id_categoria, id_empresa, nombre, descripcion)
SELECT 6, 2, 'Pinturas', 'Pinturas y accesorios'
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE id_categoria = 6);
INSERT INTO categoria (id_categoria, id_empresa, nombre, descripcion)
SELECT 7, 3, 'Medicamentos', 'Medicamentos de venta libre'
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE id_categoria = 7);
INSERT INTO categoria (id_categoria, id_empresa, nombre, descripcion)
SELECT 8, 4, 'Abarrotes', 'Productos de consumo diario'
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE id_categoria = 8);


INSERT INTO producto (id_producto, id_empresa, id_categoria, item, nombre, descripcion, precio, stock, stock_minimo, activo)
SELECT 1, 1, 1, 'P-0114', 'Resma papel bond carta', 'Resma de 500 hojas tamano carta', 4.50, 86, 20, true
WHERE NOT EXISTS (SELECT 1 FROM producto WHERE id_producto = 1);
INSERT INTO producto (id_producto, id_empresa, id_categoria, item, nombre, descripcion, precio, stock, stock_minimo, activo)
SELECT 2, 1, 2, 'P-0231', 'Cuaderno rayado 100 h', 'Cuaderno escolar de 100 hojas', 1.25, 240, 50, true
WHERE NOT EXISTS (SELECT 1 FROM producto WHERE id_producto = 2);
INSERT INTO producto (id_producto, id_empresa, id_categoria, item, nombre, descripcion, precio, stock, stock_minimo, activo)
SELECT 3, 1, 3, 'P-0087', 'Caja de lapiceros azul', 'Caja con 12 lapiceros tinta azul', 2.80, 34, 15, true
WHERE NOT EXISTS (SELECT 1 FROM producto WHERE id_producto = 3);
INSERT INTO producto (id_producto, id_empresa, id_categoria, item, nombre, descripcion, precio, stock, stock_minimo, activo)
SELECT 4, 1, 4, 'P-0356', 'Folder manila carta', 'Folder manila tamano carta', 0.15, 6, 100, true
WHERE NOT EXISTS (SELECT 1 FROM producto WHERE id_producto = 4);
INSERT INTO producto (id_producto, id_empresa, id_categoria, item, nombre, descripcion, precio, stock, stock_minimo, activo)
SELECT 5, 1, 1, 'P-0402', 'Engrapadora metalica', 'Engrapadora de escritorio', 6.90, 18, 5, true
WHERE NOT EXISTS (SELECT 1 FROM producto WHERE id_producto = 5);
INSERT INTO producto (id_producto, id_empresa, id_categoria, item, nombre, descripcion, precio, stock, stock_minimo, activo)
SELECT 6, 1, 3, 'P-0298', 'Marcador permanente', 'Marcador permanente punta gruesa', 1.10, 62, 20, true
WHERE NOT EXISTS (SELECT 1 FROM producto WHERE id_producto = 6);
INSERT INTO producto (id_producto, id_empresa, id_categoria, item, nombre, descripcion, precio, stock, stock_minimo, activo)
SELECT 7, 1, 1, 'P-0019', 'Cinta adhesiva angosta', 'Cinta adhesiva transparente', 0.90, 0, 10, false
WHERE NOT EXISTS (SELECT 1 FROM producto WHERE id_producto = 7);


INSERT INTO producto (id_producto, id_empresa, id_categoria, item, nombre, descripcion, precio, stock, stock_minimo, activo)
SELECT 8, 2, 5, 'F-1001', 'Martillo de una', 'Martillo carpintero 16 oz', 8.75, 24, 5, true
WHERE NOT EXISTS (SELECT 1 FROM producto WHERE id_producto = 8);
INSERT INTO producto (id_producto, id_empresa, id_categoria, item, nombre, descripcion, precio, stock, stock_minimo, activo)
SELECT 9, 2, 6, 'F-1042', 'Galon pintura blanca', 'Pintura latex blanca de un galon', 18.50, 40, 10, true
WHERE NOT EXISTS (SELECT 1 FROM producto WHERE id_producto = 9);
INSERT INTO producto (id_producto, id_empresa, id_categoria, item, nombre, descripcion, precio, stock, stock_minimo, activo)
SELECT 10, 2, 5, 'F-1108', 'Libra de clavos 2 pulgadas', 'Clavos de acero de 2 pulgadas', 1.35, 120, 30, true
WHERE NOT EXISTS (SELECT 1 FROM producto WHERE id_producto = 10);


INSERT INTO producto (id_producto, id_empresa, id_categoria, item, nombre, descripcion, precio, stock, stock_minimo, activo)
SELECT 11, 3, 7, 'M-2001', 'Acetaminofen 500 mg', 'Caja con 10 tabletas', 1.75, 200, 40, true
WHERE NOT EXISTS (SELECT 1 FROM producto WHERE id_producto = 11);
INSERT INTO producto (id_producto, id_empresa, id_categoria, item, nombre, descripcion, precio, stock, stock_minimo, activo)
SELECT 12, 3, 7, 'M-2015', 'Alcohol gel 250 ml', 'Alcohol en gel antibacterial', 2.25, 85, 20, true
WHERE NOT EXISTS (SELECT 1 FROM producto WHERE id_producto = 12);


INSERT INTO cliente (id_cliente, id_empresa, nombre, dui_nit, direccion, telefono)
SELECT 1, 1, 'Maria Lopez Hernandez', '04512236-8', 'Col. Las Flores, Aguilares', '7412-8890'
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE id_cliente = 1);
INSERT INTO cliente (id_cliente, id_empresa, nombre, dui_nit, direccion, telefono)
SELECT 2, 1, 'Colegio San Jose', '0614-120518-102-4', 'Av. Principal #22, Aguilares', '2345-1180'
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE id_cliente = 2);
INSERT INTO cliente (id_cliente, id_empresa, nombre, dui_nit, direccion, telefono)
SELECT 3, 1, 'Carlos Rivas Mejia', '03998741-2', 'Bo. El Centro, Aguilares', '7788-2301'
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE id_cliente = 3);
INSERT INTO cliente (id_cliente, id_empresa, nombre, dui_nit, direccion, telefono)
SELECT 4, 2, 'Constructora del Norte', '0614-090714-101-9', 'Guazapa', '2374-1100'
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE id_cliente = 4);

-- Ajustar las secuencias de PostgreSQL para que los ids autogenerados
-- continuen despues de los datos de prueba.
SELECT setval(pg_get_serial_sequence('empresa', 'id_empresa'), COALESCE((SELECT MAX(id_empresa) FROM empresa), 1));
SELECT setval(pg_get_serial_sequence('usuario', 'id_usuario'), COALESCE((SELECT MAX(id_usuario) FROM usuario), 1));
SELECT setval(pg_get_serial_sequence('categoria', 'id_categoria'), COALESCE((SELECT MAX(id_categoria) FROM categoria), 1));
SELECT setval(pg_get_serial_sequence('producto', 'id_producto'), COALESCE((SELECT MAX(id_producto) FROM producto), 1));
SELECT setval(pg_get_serial_sequence('cliente', 'id_cliente'), COALESCE((SELECT MAX(id_cliente) FROM cliente), 1));
