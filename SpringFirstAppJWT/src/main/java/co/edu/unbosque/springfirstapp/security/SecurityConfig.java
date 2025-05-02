package co.edu.unbosque.springfirstapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Clase de configuración de seguridad para la aplicación.
 * Configura la autenticación y autorización basada en JWT.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Filtro de autenticación JWT que procesa los tokens en las solicitudes.
     */
    private final JwtAuthenticationFilter jwtAuthFilter;

    /**
     * Servicio que carga los detalles del usuario para la autenticación.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Constructor que inicializa los componentes necesarios para la seguridad.
     * 
     * @param jwtAuthFilter Filtro para procesar tokens JWT
     * @param userDetailsService Servicio para cargar detalles de usuarios
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, UserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configura la cadena de filtros de seguridad HTTP.
     * Define reglas de acceso, manejo de sesiones y filtros de autenticación.
     * 
     * @param http Configuración de seguridad HTTP
     * @return Cadena de filtros de seguridad configurada
     * @throws Exception Si ocurre un error durante la configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/user/getall", "/user/count", "/user/exists/**", "/user/getbyid/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/user/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configura el proveedor de autenticación.
     * Establece el servicio de detalles de usuario y el codificador de contraseñas.
     * 
     * @return Proveedor de autenticación configurado
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Configura el gestor de autenticación.
     * 
     * @param config Configuración de autenticación
     * @return Gestor de autenticación
     * @throws Exception Si ocurre un error durante la configuración
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configura el codificador de contraseñas.
     * Utiliza BCrypt para el hash de contraseñas.
     * 
     * @return Codificador de contraseñas BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
