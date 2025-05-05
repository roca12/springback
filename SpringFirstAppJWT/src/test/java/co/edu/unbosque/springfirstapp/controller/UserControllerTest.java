package co.edu.unbosque.springfirstapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import co.edu.unbosque.springfirstapp.dto.UserDTO;
import co.edu.unbosque.springfirstapp.model.User.Role;
import co.edu.unbosque.springfirstapp.service.UserService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Clase de prueba para el controlador de usuarios.
 *
 * <p>Esta clase contiene pruebas para verificar que el controlador de usuarios funciona
 * correctamente y maneja adecuadamente las solicitudes HTTP.
 *
 * @author Universidad El Bosque
 * @version 0.1
 */
@SpringBootTest
class UserControllerTest {

  @Mock private UserService userService;

  @InjectMocks private UserController userController;

  private UserDTO testUser;

  /** Configura los objetos necesarios antes de cada prueba. */
  @BeforeEach
  public void setup() {
    testUser = new UserDTO();
    testUser.setUsername("testuser");
    testUser.setPassword("Password123!");
    testUser.setRole(Role.USER);
  }

  /**
   * Prueba la creación de un nuevo usuario con datos JSON válidos.
   *
   * <p>Esta prueba verifica que el método createNewWithJSON devuelve un estado HTTP 201 y un
   * mensaje de éxito cuando se crea un usuario correctamente.
   */
  @Test
  public void testCreateNewWithJSONSuccess() {
    when(userService.create(any(UserDTO.class))).thenReturn(0);

    ResponseEntity<String> response = userController.createNewWithJSON(testUser);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals("Usuario creado exitosamente", response.getBody());
  }

  /**
   * Prueba la creación de un nuevo usuario con un nombre de usuario que ya existe.
   *
   * <p>Esta prueba verifica que el método createNewWithJSON devuelve un estado HTTP 406 y un
   * mensaje de error cuando se intenta crear un usuario con un nombre que ya existe.
   */
  @Test
  public void testCreateNewWithJSONUserExists() {
    when(userService.create(any(UserDTO.class))).thenReturn(1);

    ResponseEntity<String> response = userController.createNewWithJSON(testUser);

    assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    assertEquals(
        "Error al crear el usuario, posiblemente el nombre de usuario ya está en uso",
        response.getBody());
  }

  /**
   * Prueba la obtención de todos los usuarios.
   *
   * <p>Esta prueba verifica que el método getAll devuelve una lista de usuarios y un estado HTTP
   * 200 cuando hay usuarios en el sistema.
   */
  @Test
  public void testGetAllUsers() {
    List<UserDTO> users = Arrays.asList(testUser);
    when(userService.getAll()).thenReturn(users);

    ResponseEntity<List<UserDTO>> response = userController.getAll();

    assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
    assertEquals("testuser", response.getBody().get(0).getUsername());
  }
}
