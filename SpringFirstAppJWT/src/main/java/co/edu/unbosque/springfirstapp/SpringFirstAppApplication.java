package co.edu.unbosque.springfirstapp;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Clase principal de la aplicación Spring Boot para Spring First App. Esta clase sirve como punto
 * de entrada para la aplicación y configura los beans necesarios para el contexto de la aplicación.
 *
 * @author Universidad El Bosque
 * @version 0.1
 */
@SpringBootApplication
public class SpringFirstAppApplication {

  /**
   * Método principal que inicia la aplicación Spring Boot.
   *
   * @param args Argumentos de línea de comandos pasados a la aplicación
   */
  public static void main(String[] args) {
    SpringApplication.run(SpringFirstAppApplication.class, args);
  }

  /**
   * Crea y configura un bean ModelMapper para el mapeo de objetos. Este bean se utiliza en toda la
   * aplicación para mapear entre DTOs y objetos de entidad.
   *
   * @return Instancia configurada de ModelMapper
   */
  @Bean
  public ModelMapper getModelMapper() {
    return new ModelMapper();
  }

  // Referencia: https://mkyong.com/java/how-to-send-http-request-getpost-in-java/

}
