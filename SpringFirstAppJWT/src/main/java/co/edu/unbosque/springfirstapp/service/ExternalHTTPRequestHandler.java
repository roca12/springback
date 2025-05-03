package co.edu.unbosque.springfirstapp.service;

import java.net.http.HttpClient;
import java.time.Duration;

public class ExternalHTTPRequestHandler {

  private static final HttpClient HTTP_CLIENT =
      HttpClient.newBuilder()
          .version(HttpClient.Version.HTTP_2)
          .connectTimeout(Duration.ofSeconds(10))
          .build();
}
