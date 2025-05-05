package co.edu.unbosque.springfirstapp;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Clase inicializadora de Servlet para desplegar la aplicación Spring Boot como un archivo WAR en
 * un contenedor de servlet externo.
 *
 * <p>Esta clase extiende SpringBootServletInitializer, que es un WebApplicationInitializer opinado
 * para ejecutar una SpringApplication desde un despliegue WAR tradicional.
 *
 * @author Universidad El Bosque
 * @version 0.1
 */
public class ServletInitializer extends SpringBootServletInitializer {

  /**
   * Configura la aplicación especificando la clase principal de la aplicación.
   *
   * @param application El SpringApplicationBuilder a configurar
   * @return El SpringApplicationBuilder configurado
   */
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(SpringFirstAppApplication.class);
  }
}
