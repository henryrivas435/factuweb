package sv.com.factuweb.api.enums;

/** Roles definidos en el sistema. Cada usuario pertenece a un unico negocio. */
public enum Rol {
    /** Registra el negocio, administra stock, clientes, vendedores y reportes. */
    ADMINISTRADOR,
    /** Solo puede emitir facturas y consultar stock y clientes. */
    VENDEDOR
}
