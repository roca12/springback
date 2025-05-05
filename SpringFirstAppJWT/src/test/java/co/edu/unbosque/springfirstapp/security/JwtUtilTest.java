package co.edu.unbosque.springfirstapp.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import co.edu.unbosque.springfirstapp.model.User;
import co.edu.unbosque.springfirstapp.model.User.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Clase de prueba para el componente JwtUtil.
 *
 * <p>Esta clase prueba la funcionalidad de generación y validación de tokens JWT, asegurando que la
 * información del usuario se codifique y extraiga correctamente de los tokens.
 *
 * @author Universidad El Bosque
 * @version 0.1
 */
@SpringBootTest
 class JwtUtilTest {

  @Autowired private JwtUtil jwtUtil;

  /**
   * Prueba que el rol del usuario se incluya correctamente en el token JWT generado y que pueda ser
   * extraído.
   *
   * <p>Esta prueba: 1. Crea un usuario de prueba con rol ADMIN 2. Genera un token JWT para el
   * usuario 3. Extrae el rol del token 4. Verifica que el rol extraído coincida con el rol original
   */
  @Test
   void testRoleIsIncludedInJwtToken() {
    // Crear un usuario de prueba con rol ADMIN
    User user = new User("testuser", "password");
    user.setRole(Role.ADMIN);

    // Generar token
    String token = jwtUtil.generateToken(user);

    // Imprimir token para depuración
    System.out.println("[DEBUG_LOG] Token generado: " + token);

    // Extraer rol del token
    String role = jwtUtil.extractRole(token);

    // Imprimir rol extraído para depuración
    System.out.println("[DEBUG_LOG] Rol extraído: " + role);

    // Verificar que el rol se incluya correctamente en el token
    assertNotNull(role, "El rol no debe ser nulo");
    assertEquals("ADMIN", role, "El rol debe ser ADMIN");
  }
}
