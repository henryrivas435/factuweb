package sv.com.factuweb.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import sv.com.factuweb.api.security.JwtAuthenticationFilter;
import sv.com.factuweb.api.security.UsuarioDetailsService;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuracion de seguridad.
 *
 * La API no usa sesiones: cada peticion se autentica con su token JWT.
 * Las rutas de /api/auth y /api/publico quedan abiertas porque corresponden
 * a la parte publica del sitio (registro de negocio, inicio de sesion y
 * verificacion de facturas por QR).
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final UsuarioDetailsService usuarioDetailsService;
    private final ObjectMapper mapper = new ObjectMapper();

    public SecurityConfig(JwtAuthenticationFilter jwtFilter,
                          UsuarioDetailsService usuarioDetailsService) {
        this.jwtFilter = jwtFilter;
        this.usuarioDetailsService = usuarioDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            /* La API no usa formularios ni cookies de sesion: CSRF no aplica.
               Si quedara activo, todo POST sin token CSRF respondería 403. */
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth
                /* El navegador envia OPTIONS antes de algunas peticiones. */
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                /* Parte publica: no requiere sesion. */
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/publico/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/facturas/verificar/**").permitAll()
                .requestMatchers("/error").permitAll()

                /* Las imagenes (logos y fotos) se muestran tambien en la
                   pagina publica, asi que se sirven sin token. */
                .requestMatchers(HttpMethod.GET, "/archivos/**").permitAll()

                .anyRequest().authenticated()
            )

            /* Sin esto, Spring Security responde 403 sin explicacion cuando
               falta el token. Con esto responde 401 y un JSON entendible. */
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, e) ->
                        escribirError(res, HttpServletResponse.SC_UNAUTHORIZED,
                                "Debe iniciar sesión para acceder a este recurso"))
                .accessDeniedHandler((req, res, e) ->
                        escribirError(res, HttpServletResponse.SC_FORBIDDEN,
                                "No tiene permisos para realizar esta acción"))
            )

            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /** Devuelve el error con el mismo formato que usa el resto de la API. */
    private void escribirError(HttpServletResponse res, int estado, String mensaje)
            throws java.io.IOException {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("momento", LocalDateTime.now().toString());
        cuerpo.put("estado", estado);
        cuerpo.put("mensaje", mensaje);

        res.setStatus(estado);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write(mapper.writeValueAsString(cuerpo));
    }

    /**
     * El filtro JWT esta anotado con @Component, asi que Spring Boot lo
     * registraria ademas como filtro global del servlet, ejecutandose dos
     * veces por peticion. Aqui se desactiva ese registro automatico: el
     * filtro solo debe correr dentro de la cadena de seguridad.
     */
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> evitarRegistroDoble(
            JwtAuthenticationFilter filtro) {
        FilterRegistrationBean<JwtAuthenticationFilter> registro =
                new FilterRegistrationBean<>(filtro);
        registro.setEnabled(false);
        return registro;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(usuarioDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** Permite que el frontend de React consuma la API. */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of(
                "http://localhost:*",
                "http://127.0.0.1:*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
