package co.edu.unbosque.springfirstapp.exception;

/**
 * Excepción que se lanza cuando una contraseña no cumple con los requisitos de seguridad.
 *
 * <p>Esta excepción se utiliza para indicar que una contraseña no cumple con los estándares mínimos
 * de seguridad requeridos por la aplicación, como longitud mínima, inclusión de letras mayúsculas y
 * minúsculas, números y símbolos.
 *
 * @author Universidad El Bosque
 * @version 0.1
 */
public class PasswordNotValidException extends Exception {

  /** Número de serie para la serialización. */
  private static final long serialVersionUID = -1290628531913444044L;

  /**
   * Constructor por defecto que inicializa la excepción con un mensaje de error estándar.
   *
   * <p>El mensaje describe los requisitos mínimos que debe cumplir una contraseña válida.
   */
  public PasswordNotValidException() {
    super(
        "La contraseña no cumple con el estándar "
            + "(mínimo 8 caracteres, al menos una letra minúscula, "
            + "al menos una letra mayúscula, "
            + "al menos un número y al menos un símbolo).");
  }
}
