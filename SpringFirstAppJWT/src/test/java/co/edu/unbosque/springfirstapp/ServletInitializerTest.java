package co.edu.unbosque.springfirstapp;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Clase de prueba para el inicializador de Servlet.
 *
 * <p>Esta clase contiene pruebas para verificar que el ServletInitializer configura correctamente
 * la aplicación Spring Boot.
 *
 * @author Universidad El Bosque
 * @version 0.1
 */
@SpringBootTest
class ServletInitializerTest {

  /**
   * Prueba que el método configure devuelve un SpringApplicationBuilder configurado.
   *
   * <p>Esta prueba verifica que el método configure del ServletInitializer configura correctamente
   * el SpringApplicationBuilder con la clase principal de la aplicación.
   */
  @Test
  void testConfigure() {
    ServletInitializer initializer = new ServletInitializer();
    SpringApplicationBuilder builder = new SpringApplicationBuilder();

    SpringApplicationBuilder result = initializer.configure(builder);

    assertNotNull(result, "El SpringApplicationBuilder configurado no debe ser nulo");
  }
}
