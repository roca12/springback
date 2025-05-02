package co.edu.unbosque.springfirstapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Clase principal de prueba para la aplicación Spring First App.
 * 
 * Esta clase contiene pruebas para verificar que el contexto de la aplicación Spring
 * se carga correctamente y que la funcionalidad básica funciona como se espera.
 * 
 * @author Universidad El Bosque
 * @version 0.1
 */
@SpringBootTest
class SpringFirstAppApplicationTests {

	/**
	 * Prueba que el contexto de la aplicación Spring se carga correctamente.
	 * 
	 * Esta es una prueba básica que verifica que el contexto de Spring puede cargarse
	 * sin errores, lo cual es una comprobación fundamental para una aplicación Spring Boot.
	 */
	@Test
	void contextLoads() {
		assertEquals(0, 0);
	}

	/**
	 * Prueba que el bean ModelMapper se crea correctamente.
	 * 
	 * Esta prueba verifica que el bean ModelMapper definido en la aplicación
	 * se crea correctamente y puede ser utilizado para mapear objetos.
	 */
	@Test
	void testModelMapperBean() {
		SpringFirstAppApplication app = new SpringFirstAppApplication();
		assertNotNull(app.getModelMapper(), "El bean ModelMapper no debe ser nulo");
	}

}
