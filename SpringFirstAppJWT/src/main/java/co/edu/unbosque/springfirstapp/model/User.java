package co.edu.unbosque.springfirstapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Clase que representa un usuario en el sistema. Implementa UserDetails para integrarse con Spring
 * Security.
 *
 * <p>Esta entidad se almacena en la tabla "useraccount" en la base de datos.
 */
@Entity
@Table(name = "useraccount")
public class User implements UserDetails {

  /** Número de versión para la serialización. */
  private static final long serialVersionUID = 1L;

  /** Identificador único del usuario. Se genera automáticamente con estrategia de identidad. */
  private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

  /** Nombre de usuario único en el sistema. */
  @Column(unique = true)
  private String username;

  /** Contraseña del usuario (almacenada con encriptación). */
  private String password;

  /** Rol del usuario en el sistema (USER o ADMIN). */
  @Enumerated(EnumType.STRING)
  private Role role;

  /** Indica si la cuenta del usuario no ha expirado. */
  private boolean accountNonExpired;

  /** Indica si la cuenta del usuario no está bloqueada. */
  private boolean accountNonLocked;

  /** Indica si las credenciales del usuario no han expirado. */
  private boolean credentialsNonExpired;

  /** Indica si la cuenta del usuario está habilitada. */
  private boolean enabled;

  /**
   * Constructor por defecto. Inicializa un usuario con valores predeterminados: - Cuenta no
   * expirada - Cuenta no bloqueada - Credenciales no expiradas - Cuenta habilitada - Rol de usuario
   * normal (USER)
   */
  public User() {
    this.accountNonExpired = true;
    this.accountNonLocked = true;
    this.credentialsNonExpired = true;
    this.enabled = true;
    this.role = Role.USER;
  }

  /**
   * Constructor con nombre de usuario y contraseña.
   *
   * @param username Nombre de usuario
   * @param password Contraseña del usuario
   */
  public User(String username, String password) {
    this();
    this.username = username;
    this.password = password;
  }

  /**
   * Constructor con nombre de usuario, contraseña y rol específico.
   *
   * @param username Nombre de usuario
   * @param password Contraseña del usuario
   * @param rol Rol asignado al usuario
   */
  public User(String username, String password, Role rol) {
    this();
    this.username = username;
    this.password = password;
    this.role = rol;
  }

  /**
   * Enumeración que define los roles disponibles en el sistema.
   *
   * <p>USER: Usuario regular con permisos básicos. ADMIN: Administrador con permisos completos.
   */
  public enum Role {
    /** Usuario regular con permisos básicos */
    USER,
    /** Administrador con permisos completos */
    ADMIN
  }

  /**
   * Obtiene las autoridades (roles) asignadas al usuario. Implementación del método de la interfaz
   * UserDetails.
   *
   * @return Colección de autoridades del usuario con el prefijo "ROLE_"
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
  }

  /**
   * Verifica si la cuenta del usuario no ha expirado. Implementación del método de la interfaz
   * UserDetails.
   *
   * @return true si la cuenta no ha expirado, false en caso contrario
   */
  @Override
  public boolean isAccountNonExpired() {
    return accountNonExpired;
  }

  /**
   * Verifica si la cuenta del usuario no está bloqueada. Implementación del método de la interfaz
   * UserDetails.
   *
   * @return true si la cuenta no está bloqueada, false en caso contrario
   */
  @Override
  public boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  /**
   * Verifica si las credenciales del usuario no han expirado. Implementación del método de la
   * interfaz UserDetails.
   *
   * @return true si las credenciales no han expirado, false en caso contrario
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  /**
   * Verifica si la cuenta del usuario está habilitada. Implementación del método de la interfaz
   * UserDetails.
   *
   * @return true si la cuenta está habilitada, false en caso contrario
   */
  @Override
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Obtiene el rol del usuario.
   *
   * @return El rol asignado al usuario
   */
  public Role getRole() {
    return role;
  }

  /**
   * Establece el rol del usuario.
   *
   * @param role El nuevo rol a asignar
   */
  public void setRole(Role role) {
    this.role = role;
  }

  /**
   * Establece si la cuenta del usuario ha expirado o no.
   *
   * @param accountNonExpired true si la cuenta no ha expirado, false si ha expirado
   */
  public void setAccountNonExpired(boolean accountNonExpired) {
    this.accountNonExpired = accountNonExpired;
  }

  /**
   * Establece si la cuenta del usuario está bloqueada o no.
   *
   * @param accountNonLocked true si la cuenta no está bloqueada, false si está bloqueada
   */
  public void setAccountNonLocked(boolean accountNonLocked) {
    this.accountNonLocked = accountNonLocked;
  }

  /**
   * Establece si las credenciales del usuario han expirado o no.
   *
   * @param credentialsNonExpired true si las credenciales no han expirado, false si han expirado
   */
  public void setCredentialsNonExpired(boolean credentialsNonExpired) {
    this.credentialsNonExpired = credentialsNonExpired;
  }

  /**
   * Establece si la cuenta del usuario está habilitada o no.
   *
   * @param enabled true si la cuenta está habilitada, false si está deshabilitada
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * Obtiene el identificador único del usuario.
   *
   * @return El ID del usuario
   */
  public Long getId() {
    return id;
  }

  /**
   * Establece el identificador único del usuario.
   *
   * @param id El nuevo ID a asignar
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Obtiene el nombre de usuario. Implementación del método de la interfaz UserDetails.
   *
   * @return El nombre de usuario
   */
  public String getUsername() {
    return username;
  }

  /**
   * Establece el nombre de usuario.
   *
   * @param username El nuevo nombre de usuario
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Obtiene la contraseña del usuario. Implementación del método de la interfaz UserDetails.
   *
   * @return La contraseña del usuario
   */
  public String getPassword() {
    return password;
  }

  /**
   * Establece la contraseña del usuario.
   *
   * @param password La nueva contraseña
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Calcula el código hash para este usuario. Se basa en los campos id, password y username.
   *
   * @return El código hash calculado
   */
  @Override
  public int hashCode() {
    return Objects.hash(id, password, username);
  }

  /**
   * Compara este usuario con otro objeto para determinar si son iguales. Dos usuarios son iguales
   * si tienen el mismo id, password y username.
   *
   * @param obj El objeto a comparar con este usuario
   * @return true si los objetos son iguales, false en caso contrario
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    User other = (User) obj;
    return Objects.equals(id, other.id)
        && Objects.equals(password, other.password)
        && Objects.equals(username, other.username);
  }

  /**
   * Devuelve una representación en cadena de texto de este usuario. Incluye el id, username y
   * password.
   *
   * @return Una cadena que representa este usuario
   */
  @Override
  public String toString() {
    return "User [id=" + id + ", username=" + username + ", password=" + password + "]";
  }
}
