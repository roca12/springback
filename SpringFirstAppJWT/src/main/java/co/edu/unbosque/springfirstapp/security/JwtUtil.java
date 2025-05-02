package co.edu.unbosque.springfirstapp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import co.edu.unbosque.springfirstapp.model.User;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Clase utilitaria para operaciones con JSON Web Tokens (JWT).
 * Proporciona métodos para generar, validar y extraer información de tokens JWT.
 */
@Component
public class JwtUtil {

    /**
     * Tiempo de validez del token JWT en milisegundos (24 horas).
     */
    private static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60 * 1000; // 24 horas

    /**
     * Clave secreta utilizada para firmar los tokens JWT.
     * Se puede configurar en las propiedades de la aplicación.
     */
    @Value("${jwt.secret:defaultSecretKeyWhichShouldBeAtLeast32CharactersLong}")
    private String secret;

    /**
     * Obtiene la clave de firma para los tokens JWT.
     * 
     * @return Clave de firma generada a partir del secreto configurado
     */
    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extrae el nombre de usuario del token JWT.
     * 
     * @param token Token JWT del cual extraer el nombre de usuario
     * @return Nombre de usuario contenido en el token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae la fecha de expiración del token JWT.
     * 
     * @param token Token JWT del cual extraer la fecha de expiración
     * @return Fecha de expiración del token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrae el rol del usuario del token JWT.
     * 
     * @param token Token JWT del cual extraer el rol
     * @return Rol del usuario contenido en el token
     */
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    /**
     * Método genérico para extraer cualquier reclamación (claim) del token JWT.
     * 
     * @param token Token JWT del cual extraer la reclamación
     * @param claimsResolver Función para resolver la reclamación específica
     * @return Valor de la reclamación extraída
     * @param <T> Tipo de dato de la reclamación a extraer
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrae todas las reclamaciones (claims) del token JWT.
     * 
     * @param token Token JWT del cual extraer todas las reclamaciones
     * @return Objeto Claims que contiene todas las reclamaciones del token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Verifica si un token JWT ha expirado.
     * 
     * @param token Token JWT a verificar
     * @return true si el token ha expirado, false en caso contrario
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Genera un token JWT para un usuario.
     * 
     * @param userDetails Detalles del usuario para el cual generar el token
     * @return Token JWT generado
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", userDetails.getAuthorities());

        // Añadir rol a las reclamaciones si userDetails es de nuestra clase User
        if (userDetails instanceof User) {
            User user = (User) userDetails;
            claims.put("role", user.getRole().name());
        }

        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Crea un token JWT con las reclamaciones especificadas.
     * 
     * @param claims Reclamaciones a incluir en el token
     * @param subject Asunto del token (normalmente el nombre de usuario)
     * @return Token JWT creado
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Valida un token JWT para un usuario específico.
     * 
     * @param token Token JWT a validar
     * @param userDetails Detalles del usuario contra los cuales validar el token
     * @return true si el token es válido para el usuario, false en caso contrario
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
