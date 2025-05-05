package co.edu.unbosque.springfirstapp.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Clase de prueba para la excepción PasswordNotValidException.
 *
 * <p>Esta clase contiene pruebas para verificar que la excepción PasswordNotValidException se
 * comporta correctamente y proporciona el mensaje de error adecuado.
 *
 * @author Universidad El Bosque
 * @version 0.1
 */
@SpringBootTest
 class PasswordNotValidExceptionTest {

  /**
   * Prueba que la excepción se crea correctamente con el mensaje de error adecuado.
   *
   * <p>Esta prueba verifica que al crear una instancia de PasswordNotValidException, el mensaje de
   * error contiene la información esperada sobre los requisitos de contraseña.
   */
  @Test
   void testExceptionMessage() {
    PasswordNotValidException exception = new PasswordNotValidException();

    assertNotNull(exception.getMessage(), "El mensaje de error no debe ser nulo");
    assertEquals(
        "La contraseña no cumple con el estándar "
            + "(mínimo 8 caracteres, al menos una letra minúscula, "
            + "al menos una letra mayúscula, "
            + "al menos un número y al menos un símbolo).",
        exception.getMessage(),
        "El mensaje de error debe describir los requisitos de la contraseña");
  }
}
