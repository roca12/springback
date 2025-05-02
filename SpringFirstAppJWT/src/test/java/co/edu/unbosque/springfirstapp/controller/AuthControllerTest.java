package co.edu.unbosque.springfirstapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.unbosque.springfirstapp.dto.UserDTO;
import co.edu.unbosque.springfirstapp.model.User;
import co.edu.unbosque.springfirstapp.model.User.Role;
import co.edu.unbosque.springfirstapp.security.JwtUtil;

/**
 * Clase de prueba para el AuthController.
 * 
 * Esta clase prueba la funcionalidad de autenticación proporcionada por el AuthController,
 * incluyendo el inicio de sesión y el manejo de roles de usuario.
 * 
 * @author Universidad El Bosque
 * @version 0.1
 */
@SpringBootTest
public class AuthControllerTest {

    @Autowired
    private AuthController authController;

    @Autowired
    private JwtUtil jwtUtil;

    @MockBean
    private AuthenticationManager authenticationManager;

    /**
     * Prueba que el endpoint de inicio de sesión incluya correctamente el rol del usuario en la respuesta.
     * 
     * Esta prueba:
     * 1. Crea un usuario de prueba con rol ADMIN
     * 2. Simula el proceso de autenticación
     * 3. Llama al endpoint de inicio de sesión
     * 4. Verifica que la respuesta contenga el rol correcto
     * 
     * @throws Exception si falla el procesamiento JSON
     */
    @Test
    public void testLoginReturnsRoleInResponse() throws Exception {
        // Crear usuario de prueba con rol ADMIN
        User user = new User("testuser", "password");
        user.setRole(Role.ADMIN);

        // Crear objeto de autenticación
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        // Simular el gestor de autenticación para que devuelva nuestra autenticación
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        // Crear solicitud de inicio de sesión
        UserDTO loginRequest = new UserDTO();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        // Llamar al endpoint de inicio de sesión
        ResponseEntity<?> response = authController.login(loginRequest);

        // Verificar que el estado de la respuesta sea OK
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El estado de la respuesta debe ser OK");

        // Convertir el cuerpo de la respuesta a JSON para inspección
        ObjectMapper mapper = new ObjectMapper();
        String responseJson = mapper.writeValueAsString(response.getBody());

        // Imprimir respuesta para depuración
        System.out.println("[DEBUG_LOG] Respuesta: " + responseJson);

        // Verificar que la respuesta contenga el rol
        assertNotNull(responseJson, "La respuesta no debe ser nula");
        assertEquals(true, responseJson.contains("\"role\":\"ADMIN\""), "La respuesta debe contener el rol ADMIN");
    }
}
