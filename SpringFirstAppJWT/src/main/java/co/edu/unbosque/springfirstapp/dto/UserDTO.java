package co.edu.unbosque.springfirstapp.dto;

import co.edu.unbosque.springfirstapp.model.User.Role;
import java.util.Objects;

/**
 * Clase de Objeto de Transferencia de Datos (DTO) para usuarios. Utilizada para transferir datos de
 * usuario entre capas de la aplicación, especialmente en operaciones de API.
 */
public class UserDTO {

  /** Identificador único del usuario. */
  private Long id;

  /** Nombre de usuario para autenticación. */
  private String username;

  /** Contraseña del usuario. */
  private String password;

  /** Rol del usuario en el sistema (por ejemplo, USER, ADMIN). */
  private Role role;

  /** Constructor por defecto. Crea una instancia vacía de UserDTO. */
  public UserDTO() {}

  /**
   * Constructor con nombre de usuario y contraseña.
   *
   * @param username Nombre de usuario
   * @param password Contraseña del usuario
   */
  public UserDTO(String username, String password) {
    this.username = username;
    this.password = password;
  }

  /**
   * Constructor con nombre de usuario, contraseña y rol.
   *
   * @param username Nombre de usuario
   * @param password Contraseña del usuario
   * @param role Rol del usuario
   */
  public UserDTO(String username, String password, Role role) {
    this.username = username;
    this.password = password;
    this.role = role;
  }

  /**
   * Obtiene el identificador del usuario.
   *
   * @return Identificador único del usuario
   */
  public Long getId() {
    return id;
  }

  /**
   * Establece el identificador del usuario.
   *
   * @param id Identificador único a establecer
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Obtiene el nombre de usuario.
   *
   * @return Nombre de usuario
   */
  public String getUsername() {
    return username;
  }

  /**
   * Establece el nombre de usuario.
   *
   * @param username Nombre de usuario a establecer
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Obtiene la contraseña del usuario.
   *
   * @return Contraseña del usuario
   */
  public String getPassword() {
    return password;
  }

  /**
   * Establece la contraseña del usuario.
   *
   * @param password Contraseña a establecer
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Obtiene el rol del usuario.
   *
   * @return Rol del usuario
   */
  public Role getRole() {
    return role;
  }

  /**
   * Establece el rol del usuario.
   *
   * @param role Rol a establecer
   */
  public void setRole(Role role) {
    this.role = role;
  }

  /**
   * Calcula el código hash para este objeto. Utilizado para almacenar objetos en colecciones
   * basadas en hash.
   *
   * @return Código hash calculado
   */
  @Override
  public int hashCode() {
    return Objects.hash(id, password, role, username);
  }

  /**
   * Compara este objeto con otro para determinar igualdad. Dos objetos UserDTO son iguales si
   * tienen el mismo id, username, password y role.
   *
   * @param obj Objeto a comparar
   * @return true si los objetos son iguales, false en caso contrario
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    UserDTO other = (UserDTO) obj;
    return Objects.equals(id, other.id)
        && Objects.equals(password, other.password)
        && role == other.role
        && Objects.equals(username, other.username);
  }

  /**
   * Devuelve una representación en cadena de texto de este objeto. Útil para depuración y registro.
   *
   * @return Representación en cadena de texto del objeto
   */
  @Override
  public String toString() {
    return "User [id="
        + id
        + ", username="
        + username
        + ", password="
        + password
        + ", role="
        + role
        + "]";
  }
}
