package co.edu.unbosque.springfirstapp.service;

import java.net.http.HttpClient;
import java.time.Duration;

/**
 * Clase para manejar peticiones HTTP a servicios externos. Proporciona funcionalidad para realizar
 * solicitudes HTTP a APIs externas.
 */
public class ExternalHTTPRequestHandler {

  /**
   * Cliente HTTP compartido para realizar peticiones. Configurado con HTTP/2 y un tiempo de espera
   * de conexión de 10 segundos.
   */
  private static final HttpClient HTTP_CLIENT =
      HttpClient.newBuilder()
          .version(HttpClient.Version.HTTP_2)
          .connectTimeout(Duration.ofSeconds(10))
          .build();
}
