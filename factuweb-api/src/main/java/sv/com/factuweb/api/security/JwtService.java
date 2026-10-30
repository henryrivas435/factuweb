package sv.com.factuweb.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Genera y valida los tokens JWT.
 *
 * El token lleva dentro el id del usuario, su rol y su idEmpresa. Ese ultimo
 * dato es esencial: es lo que permite que cada peticion sepa a que negocio
 * pertenece quien la hizo, sin tener que consultarlo en cada llamada.
 */
@Service
public class JwtService {

    private final SecretKey clave;
    private final long expiracionMs;

    public JwtService(@Value("${factuweb.jwt.secret}") String secret,
                      @Value("${factuweb.jwt.expiracion-ms}") long expiracionMs) {
        this.clave = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiracionMs = expiracionMs;
    }

    public String generarToken(UsuarioPrincipal usuario) {
        Map<String, Object> datos = new HashMap<>();
        datos.put("idUsuario", usuario.getIdUsuario());
        datos.put("idEmpresa", usuario.getIdEmpresa());
        datos.put("rol", usuario.getRol().name());
        datos.put("nombre", usuario.getNombre());

        Date ahora = new Date();
        return Jwts.builder()
                .claims(datos)
                .subject(usuario.getEmail())
                .issuedAt(ahora)
                .expiration(new Date(ahora.getTime() + expiracionMs))
                .signWith(clave)
                .compact();
    }

    public String extraerEmail(String token) {
        return leerClaims(token).getSubject();
    }

    public boolean esValido(String token, UsuarioPrincipal usuario) {
        try {
            Claims claims = leerClaims(token);
            return claims.getSubject().equals(usuario.getEmail())
                   && claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public long getExpiracionMs() {
        return expiracionMs;
    }

    private Claims leerClaims(String token) {
        return Jwts.parser()
                .verifyWith(clave)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
