package co.edu.unbosque.springfirstapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.springfirstapp.dto.UserDTO;
import co.edu.unbosque.springfirstapp.model.User;
import co.edu.unbosque.springfirstapp.security.JwtUtil;
import co.edu.unbosque.springfirstapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador REST para la autenticación de usuarios.
 * Maneja las operaciones de inicio de sesión y registro de usuarios.
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "API para autenticación de usuarios (login y registro)")
public class AuthController {

    /**
     * Gestor de autenticación para validar credenciales de usuario.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Utilidad para operaciones con tokens JWT.
     */
    private final JwtUtil jwtUtil;

    /**
     * Servicio para operaciones relacionadas con usuarios.
     */
    private final UserService userService;

    /**
     * Constructor que inicializa las dependencias necesarias para el controlador.
     * 
     * @param authenticationManager Gestor de autenticación
     * @param jwtUtil Utilidad para tokens JWT
     * @param userService Servicio de usuarios
     */
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    /**
     * Maneja las solicitudes de inicio de sesión.
     * Autentica al usuario y genera un token JWT si las credenciales son válidas.
     * 
     * @param loginRequest DTO con las credenciales de inicio de sesión (nombre de usuario y contraseña)
     * @return ResponseEntity con el token JWT y el rol del usuario si la autenticación es exitosa,
     *         o un mensaje de error si falla
     */
    @Operation(
        summary = "Iniciar sesión de usuario",
        description = """
            Este endpoint permite a los usuarios iniciar sesión en el sistema proporcionando sus credenciales.

            **¿Qué hace?** Verifica las credenciales del usuario y, si son correctas, genera un token JWT 
            que se utilizará para autenticar solicitudes posteriores.

            **Paso a paso:**

            1. Envía tu nombre de usuario y contraseña en formato JSON
            2. Si las credenciales son correctas, recibirás un token JWT
            3. Guarda este token para usarlo en futuras peticiones
            4. Para usar el token, inclúyelo en el encabezado de autorización: `Authorization: Bearer tu_token_jwt`

            **Nota:** El token tiene un tiempo de expiración limitado. Si expira, necesitarás iniciar sesión nuevamente.
        """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Inicio de sesión exitoso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponse.class),
                examples = @ExampleObject(
                    value = """
                        {
                          "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                          "role": "ADMIN"
                        }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Credenciales inválidas",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "Nombre de usuario o contraseña inválidos o usuario no encontrado"
                )
            )
        )
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(
        @Parameter(
            description = "Credenciales de usuario para iniciar sesión", 
            required = true,
            schema = @Schema(implementation = UserDTO.class),
            examples = @ExampleObject(
                value = """
                    {
                      "username": "admin",
                      "password": "1234567890"
                    }
                """
            )
        )
        @RequestBody UserDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(userDetails);

            // Obtener el rol de userDetails si es nuestra clase User
            String role = null;
            if (userDetails instanceof User) {
                User user = (User) userDetails;
                role = user.getRole().name();
            }

            return ResponseEntity.ok(new AuthResponse(jwt, role));
        } catch (AuthenticationException e) {
           e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nombre de usuario o contraseña inválidos o usuario no encontrado" );
        }
    }

    /**
     * Maneja las solicitudes de registro de nuevos usuarios.
     * Verifica si el nombre de usuario ya existe y crea un nuevo usuario si está disponible.
     * 
     * @param registerRequest DTO con la información del nuevo usuario
     * @return ResponseEntity con un mensaje de éxito si el registro es exitoso,
     *         o un mensaje de error si falla
     */
    @Operation(
        summary = "Registrar un nuevo usuario",
        description = """
            Este endpoint permite crear una nueva cuenta de usuario en el sistema.

            **¿Qué hace?** Registra un nuevo usuario con el nombre de usuario y contraseña proporcionados. 
            Por defecto, los usuarios creados mediante este endpoint tendrán el rol USER.

            **Paso a paso:**

            1. Envía el nombre de usuario y contraseña deseados en formato JSON
            2. El sistema verificará si el nombre de usuario ya existe
            3. Si el nombre está disponible, se creará la cuenta y recibirás un mensaje de éxito
            4. Después de registrarte, puedes usar el endpoint de login para obtener un token JWT

            **Requisitos para la contraseña:** La contraseña debe tener al menos 8 caracteres.

            **Nota:** Este endpoint es público y no requiere autenticación.
        """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Usuario registrado exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "User registered successfully"
                )
            )
        ),
        @ApiResponse(
            responseCode = "409", 
            description = "El nombre de usuario ya existe",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "Username already exists"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Error al registrar el usuario",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "Error registering user"
                )
            )
        )
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(
        @Parameter(
            description = "Información del nuevo usuario", 
            required = true,
            schema = @Schema(implementation = UserDTO.class),
            examples = @ExampleObject(
                value = """
                    {
                      "username": "nuevoUsuario",
                      "password": "contraseña123"
                    }
                """
            )
        )
        @RequestBody UserDTO registerRequest) {
        // Verificar si el nombre de usuario ya existe
        if (userService.findUsernameAlreadyTaken(registerRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El nombre de usuario ya existe");
        }

        // Crear nuevo usuario
        int result = userService.create(registerRequest);
        if (result == 0) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado exitosamente");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al registrar el usuario");
        }
    }

    /**
     * Clase interna para representar la respuesta de autenticación.
     * Contiene el token JWT y el rol del usuario autenticado.
     */
    private static class AuthResponse {
        /**
         * Token JWT generado para el usuario autenticado.
         */
        private final String token;

        /**
         * Rol del usuario autenticado.
         */
        private final String role;

        /**
         * Constructor con solo token.
         * 
         * @param token Token JWT generado
         */
        public AuthResponse(String token) {
            this.token = token;
            // Extraer rol del token
            this.role = null; // Se establecerá en el constructor con el parámetro de rol
        }

        /**
         * Constructor con token y rol.
         * 
         * @param token Token JWT generado
         * @param role Rol del usuario
         */
        public AuthResponse(String token, String role) {
            this.token = token;
            this.role = role;
        }

        /**
         * Obtiene el token JWT.
         * 
         * @return Token JWT
         */
        public String getToken() {
            return token;
        }

        /**
         * Obtiene el rol del usuario.
         * 
         * @return Rol del usuario
         */
        public String getRole() {
            return role;
        }
    }
}
