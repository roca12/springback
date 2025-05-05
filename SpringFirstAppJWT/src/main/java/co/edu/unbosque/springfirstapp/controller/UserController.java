package co.edu.unbosque.springfirstapp.controller;

import co.edu.unbosque.springfirstapp.dto.UserDTO;
import co.edu.unbosque.springfirstapp.model.User.Role;
import co.edu.unbosque.springfirstapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para la gestión de usuarios. Proporciona endpoints para crear, leer, actualizar
 * y eliminar usuarios. Requiere autenticación JWT para todos los endpoints. Los endpoints /getall,
 * /count, /exists/*, /getbyid/* son accesibles para usuarios con ROLE_USER o ROLE_ADMIN. Todos los
 * demás endpoints requieren ROLE_ADMIN.
 *
 * @author Universidad El Bosque
 * @version 0.1
 */
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:8081"})
@Transactional
@Tag(name = "Gestión de Usuarios", description = "Endpoints para administrar usuarios")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

  /** Servicio para operaciones relacionadas con usuarios. */
  @Autowired private UserService userServ;

  /** Constructor por defecto. */
  public UserController() {}

  /**
   * Crea un nuevo usuario utilizando datos en formato JSON. Requiere rol ADMIN.
   *
   * @param newUser DTO con los datos del usuario a crear
   * @return ResponseEntity con mensaje de éxito o error
   */
  @Operation(
      summary = "Crear usuario (JSON)",
      description =
          """
			Este endpoint permite crear un nuevo usuario enviando datos en formato JSON.

			**¿Qué hace?** Crea un nuevo usuario con el nombre de usuario, contraseña y rol proporcionados.

			**Posibles resultados:**

			* Usuario creado exitosamente
			* Error al crear el usuario (el nombre de usuario ya existe)
			* Solicitud con caracteres inválidos

			**Nota:** Este endpoint requiere autenticación y rol ADMIN.
		""")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Usuario creado exitosamente",
            content =
                @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "Usuario creado exitosamente"))),
        @ApiResponse(
            responseCode = "406",
            description = "Error al crear el usuario",
            content =
                @Content(
                    mediaType = "application/json",
                    examples =
                        @ExampleObject(
                            value =
                                "Error al crear el usuario, posiblemente el nombre de usuario ya"
                                    + " está en uso"))),
        @ApiResponse(
            responseCode = "400",
            description = "Solicitud con caracteres inválidos",
            content =
                @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "Solicitud con caracteres inválidos")))
      })
  @PostMapping(path = "/createjson", consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<String> createNewWithJSON(
      @Parameter(
              description = "Datos del nuevo usuario",
              required = true,
              schema = @Schema(implementation = UserDTO.class),
              examples =
                  @ExampleObject(
                      value =
                          """
					{
					  "username": "nuevoUsuario",
					  "password": "contraseña123",
					  "role": "USER"
					}
				"""))
          @RequestBody
          UserDTO newUser) {
    if (newUser.getUsername().contains("<") || newUser.getUsername().contains(">")) {
      return new ResponseEntity<>("Solicitud con caracteres invalidos", HttpStatus.BAD_REQUEST);
    }
    int status = userServ.create(newUser);

    if (status == 0) {
      return new ResponseEntity<>("Usuario creado exitosamente", HttpStatus.CREATED);
    } else {
      return new ResponseEntity<>(
          "Error al crear el usuario, posiblemente el nombre de usuario ya está en uso",
          HttpStatus.NOT_ACCEPTABLE);
    }
  }

  /**
   * Crea un nuevo usuario utilizando parámetros de solicitud. Requiere rol ADMIN.
   *
   * @param username Nombre de usuario
   * @param password Contraseña
   * @param role Rol del usuario
   * @return ResponseEntity con mensaje de éxito o error
   */
  @Operation(
      summary = "Crear usuario (parámetros)",
      description =
          """
			Este endpoint permite crear un nuevo usuario enviando datos como parámetros de solicitud.

			**¿Qué hace?** Crea un nuevo usuario con el nombre de usuario, contraseña y rol proporcionados.

			**Posibles resultados:**

			* Usuario creado exitosamente
			* Error al crear el usuario (el nombre de usuario ya existe)

			**Nota:** Este endpoint requiere autenticación y rol ADMIN.
		""")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Usuario creado correctamente",
            content =
                @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "Usuario creado exitosamente"))),
        @ApiResponse(
            responseCode = "406",
            description = "Error al crear el usuario",
            content =
                @Content(
                    mediaType = "application/json",
                    examples =
                        @ExampleObject(
                            value =
                                "Error al crear el usuario, posiblemente el nombre de usuario ya"
                                    + " está en uso")))
      })
  @PostMapping(path = "/create")
  ResponseEntity<String> createNew(
      @Parameter(description = "Nombre de usuario", required = true, example = "nuevoUsuario")
          @RequestParam
          String username,
      @Parameter(description = "Contraseña", required = true, example = "contraseña123")
          @RequestParam
          String password,
      @Parameter(description = "Rol del usuario", required = true, example = "USER")
          @RequestParam(required = true)
          Role role) {
    UserDTO newUser = new UserDTO(username, password);
    if (role != null) {
      newUser.setRole(role);
    }
    int status = userServ.create(newUser);

    if (status == 0) {
      return new ResponseEntity<>("Usuario creado exitosamente", HttpStatus.CREATED);
    } else {
      return new ResponseEntity<>(
          "Error al crear el usuario, posiblemente el nombre de usuario ya está en uso",
          HttpStatus.NOT_ACCEPTABLE);
    }
  }

  /**
   * Obtiene todos los usuarios. Accesible para usuarios con rol USER o ADMIN.
   *
   * @return ResponseEntity con lista de usuarios o mensaje de no contenido
   */
  @Operation(
      summary = "Obtener todos los usuarios",
      description =
          """
			Este endpoint permite obtener una lista de todos los usuarios registrados en el sistema.

			**¿Qué hace?** Recupera todos los usuarios de la base de datos y devuelve sus datos básicos.

			**Nota:** Este endpoint requiere autenticación y es accesible para usuarios con rol USER o ADMIN.
		""")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "202",
            description = "Lista de usuarios recuperada correctamente",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class),
                    examples =
                        @ExampleObject(
                            value =
                                """
						[
						  {
						    "id": 1,
						    "username": "admin",
						    "role": "ADMIN"
						  },
						  {
						    "id": 2,
						    "username": "usuario1",
						    "role": "USER"
						  }
						]
					"""))),
        @ApiResponse(
            responseCode = "204",
            description = "No hay usuarios registrados",
            content =
                @Content(mediaType = "application/json", examples = @ExampleObject(value = "[]")))
      })
  @GetMapping("/getall")
  ResponseEntity<List<UserDTO>> getAll() {
    List<UserDTO> users = userServ.getAll();
    if (users.isEmpty()) {
      return new ResponseEntity<>(users, HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(users, HttpStatus.ACCEPTED);
    }
  }

  /**
   * Cuenta el número total de usuarios. Accesible para usuarios con rol USER o ADMIN.
   *
   * @return ResponseEntity con el número de usuarios
   */
  @Operation(
      summary = "Contar usuarios",
      description =
          """
			Este endpoint permite obtener el número total de usuarios registrados en el sistema.

			**¿Qué hace?** Cuenta todos los usuarios en la base de datos y devuelve el total.

			**Nota:** Este endpoint requiere autenticación y es accesible para usuarios con rol USER o ADMIN.
		""")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "202",
            description = "Conteo de usuarios exitoso",
            content =
                @Content(mediaType = "application/json", examples = @ExampleObject(value = "5"))),
        @ApiResponse(
            responseCode = "204",
            description = "No hay usuarios registrados",
            content =
                @Content(mediaType = "application/json", examples = @ExampleObject(value = "0")))
      })
  @GetMapping("/count")
  ResponseEntity<Long> countAll() {
    Long count = userServ.count();
    if (count == 0) {
      return new ResponseEntity<>(count, HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(count, HttpStatus.ACCEPTED);
    }
  }

  /**
   * Verifica si existe un usuario con el ID especificado. Accesible para usuarios con rol USER o
   * ADMIN.
   *
   * @param id ID del usuario a verificar
   * @return ResponseEntity con true si existe, false si no
   */
  @Operation(
      summary = "Verificar existencia de usuario",
      description =
          """
			Este endpoint permite verificar si existe un usuario con el ID especificado.

			**¿Qué hace?** Comprueba si existe un usuario con el ID proporcionado en la base de datos.

			**Nota:** Este endpoint requiere autenticación y es accesible para usuarios con rol USER o ADMIN.
		""")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "202",
            description = "Usuario encontrado",
            content =
                @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "true"))),
        @ApiResponse(
            responseCode = "204",
            description = "Usuario no encontrado",
            content =
                @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "false")))
      })
  @GetMapping("/exists/{id}")
  ResponseEntity<Boolean> exists(
      @Parameter(description = "ID del usuario a verificar", required = true, example = "1")
          @PathVariable
          Long id) {
    boolean found = userServ.exist(id);
    if (found) {
      return new ResponseEntity<>(true, HttpStatus.ACCEPTED);
    } else {
      return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
    }
  }

  /**
   * Obtiene un usuario por su ID. Accesible para usuarios con rol USER o ADMIN.
   *
   * @param id ID del usuario a buscar
   * @return ResponseEntity con el usuario encontrado o un objeto vacío si no existe
   */
  @Operation(
      summary = "Obtener usuario por ID",
      description =
          """
			Este endpoint permite obtener la información de un usuario específico mediante su ID.

			**¿Qué hace?** Busca un usuario en la base de datos por su ID y devuelve sus datos.

			**Nota:** Este endpoint requiere autenticación y es accesible para usuarios con rol USER o ADMIN.
		""")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "202",
            description = "Usuario encontrado",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class),
                    examples =
                        @ExampleObject(
                            value =
                                """
						{
						  "id": 1,
						  "username": "admin",
						  "role": "ADMIN"
						}
					"""))),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class),
                    examples = @ExampleObject(value = "{}")))
      })
  @GetMapping("/getbyid/{id}")
  ResponseEntity<UserDTO> getById(
      @Parameter(description = "ID del usuario a buscar", required = true, example = "1")
          @PathVariable
          Long id) {
    UserDTO found = userServ.getById(id);
    if (found != null) {
      return new ResponseEntity<>(found, HttpStatus.ACCEPTED);
    } else {
      return new ResponseEntity<>(new UserDTO(), HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Actualiza un usuario existente utilizando datos en formato JSON. Requiere rol ADMIN.
   *
   * @param id ID del usuario a actualizar
   * @param newUser DTO con los nuevos datos del usuario
   * @return ResponseEntity con mensaje de éxito o error
   */
  @Operation(
      summary = "Actualizar usuario (JSON)",
      description =
          """
			Este endpoint permite actualizar la información de un usuario existente mediante su ID, enviando los nuevos datos en formato JSON.

			**¿Qué hace?** Actualiza el nombre de usuario, contraseña y/o rol de un usuario existente.

			**Posibles resultados:**

			* Usuario actualizado correctamente
			* El nuevo nombre de usuario ya está en uso
			* Usuario no encontrado
			* Error en la actualización

			**Nota:** Este endpoint requiere autenticación y rol ADMIN.
		""")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "202",
            description = "Usuario actualizado correctamente",
            content =
                @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "Usuario actualizado exitosamente"))),
        @ApiResponse(
            responseCode = "226",
            description = "El nuevo nombre de usuario ya está en uso",
            content =
                @Content(
                    mediaType = "application/json",
                    examples =
                        @ExampleObject(value = "El nuevo nombre de usuario ya está en uso"))),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado",
            content =
                @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "Usuario no encontrado"))),
        @ApiResponse(
            responseCode = "400",
            description = "Error en la actualización",
            content =
                @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "Error al actualizar")))
      })
  @PutMapping(path = "/updatejson", consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<String> updateNewWithJSON(
      @Parameter(description = "ID del usuario a actualizar", required = true, example = "1")
          @RequestParam
          Long id,
      @Parameter(
              description = "Nuevos datos del usuario",
              required = true,
              schema = @Schema(implementation = UserDTO.class),
              examples =
                  @ExampleObject(
                      value =
                          """
					{
					  "username": "nuevoNombre",
					  "password": "nuevaContraseña",
					  "role": "ADMIN"
					}
				"""))
          @RequestBody
          UserDTO newUser) {

    int status = userServ.updateById(id, newUser);

    if (status == 0) {
      return new ResponseEntity<>("Usuario actualizado exitosamente", HttpStatus.ACCEPTED);
    } else if (status == 1) {
      return new ResponseEntity<>("El nuevo nombre de usuario ya está en uso", HttpStatus.IM_USED);
    } else if (status == 2) {
      return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
    } else {
      return new ResponseEntity<>("Error al actualizar", HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Actualiza un usuario por su ID, ingresando nuevo nombre de usuario, contraseña y rol. Requiere
   * rol ADMIN.
   *
   * @param id ID del usuario a eliminar
   * @param newUsername nuevo username del usuario a actualizar
   * @param newPassword nuevo password del usuario a actualizar
   * @param role nuevo rol del usuario a actualizar
   * @return ResponseEntity con mensaje de éxito o error
   */
  @Operation(
      summary = "Actualizar usuario (parámetros)",
      description =
          """
			Este endpoint permite actualizar la información de un usuario existente mediante su ID, enviando los nuevos datos como parámetros de la solicitud.

			**¿Qué hace?** Actualiza el nombre de usuario, contraseña y/o rol de un usuario existente.

			**Posibles resultados:**

			* Usuario actualizado correctamente
			* El nuevo nombre de usuario ya está en uso
			* Usuario no encontrado
			* Error en la actualización

			**Nota:** Este endpoint requiere autenticación y rol ADMIN.
		""")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "202",
            description = "Usuario actualizado correctamente",
            content =
                @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "Usuario actualizado exitosamente"))),
        @ApiResponse(
            responseCode = "226",
            description = "El nuevo nombre de usuario ya está en uso",
            content =
                @Content(
                    mediaType = "application/json",
                    examples =
                        @ExampleObject(value = "El nuevo nombre de usuario ya está en uso"))),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado",
            content =
                @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "Usuario no encontrado"))),
        @ApiResponse(
            responseCode = "400",
            description = "Error en la actualización",
            content =
                @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "Error al actualizar")))
      })
  @PutMapping(path = "/update")
  ResponseEntity<String> updateNew(
      @Parameter(description = "ID del usuario a actualizar", required = true, example = "1")
          @RequestParam
          long id,
      @Parameter(description = "Nuevo nombre de usuario", required = true, example = "nuevoNombre")
          @RequestParam
          String newUsername,
      @Parameter(description = "Nueva contraseña", required = true, example = "nuevaContraseña")
          @RequestParam
          String newPassword,
      @Parameter(description = "Nuevo rol del usuario", required = false, example = "ADMIN")
          @RequestParam(required = false)
          Role role) {
    UserDTO newUser = new UserDTO(newUsername, newPassword);
    if (role != null) {
      newUser.setRole(role);
    }

    int status = userServ.updateById(id, newUser);

    if (status == 0) {
      return new ResponseEntity<>("Usuario actualizado exitosamente", HttpStatus.ACCEPTED);
    } else if (status == 1) {
      return new ResponseEntity<>("El nuevo nombre de usuario ya está en uso", HttpStatus.IM_USED);
    } else if (status == 2) {
      return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
    } else {
      return new ResponseEntity<>("Error al actualizar", HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Elimina un usuario por su ID. Requiere rol ADMIN.
   *
   * @param id ID del usuario a eliminar
   * @return ResponseEntity con mensaje de éxito o error
   */
  @Operation(
      summary = "Eliminar usuario por ID",
      description =
          """
			Este endpoint permite eliminar un usuario existente mediante su ID.

			**¿Qué hace?** Elimina permanentemente un usuario de la base de datos.

			**Advertencia:** Esta acción no se puede deshacer. Una vez eliminado, el usuario no podrá recuperarse.

			**Nota:** Este endpoint requiere autenticación y rol ADMIN.
		""")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "202",
            description = "Usuario eliminado correctamente",
            content =
                @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "Usuario eliminado exitosamente"))),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado",
            content =
                @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "Error al eliminar")))
      })
  @DeleteMapping("/deletebyid/{id}")
  ResponseEntity<String> deleteById(
      @Parameter(description = "ID del usuario a eliminar", required = true, example = "1")
          @PathVariable
          Long id) {
    int status = userServ.deleteById(id);

    if (status == 0) {
      return new ResponseEntity<>("Usuario eliminado exitosamente", HttpStatus.ACCEPTED);
    } else {
      return new ResponseEntity<>("Error al eliminar", HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Elimina un usuario por su nombre de usuario. Requiere rol ADMIN.
   *
   * @param name Nombre del usuario a eliminar
   * @return ResponseEntity con mensaje de éxito o error
   */
  @Operation(
      summary = "Eliminar usuario por nombre",
      description =
          """
			Este endpoint permite eliminar un usuario existente mediante su nombre de usuario.

			**¿Qué hace?** Busca un usuario por su nombre y lo elimina permanentemente de la base de datos.

			**Advertencia:** Esta acción no se puede deshacer. Una vez eliminado, el usuario no podrá recuperarse.

			**Nota:** Este endpoint requiere autenticación y rol ADMIN.
		""")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "202",
            description = "Usuario eliminado correctamente",
            content =
                @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "Usuario eliminado exitosamente"))),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado",
            content =
                @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "Error al eliminar")))
      })
  @DeleteMapping("/deletebyname")
  ResponseEntity<String> deleteByName(
      @Parameter(
              description = "Nombre del usuario a eliminar",
              required = true,
              example = "usuario1")
          @RequestParam
          String name) {
    int status = userServ.deleteByUsername(name);
    if (status == 0) {
      return new ResponseEntity<>("Usuario eliminado exitosamente", HttpStatus.ACCEPTED);
    } else {
      return new ResponseEntity<>("Error al eliminar", HttpStatus.NOT_FOUND);
    }
  }
}
