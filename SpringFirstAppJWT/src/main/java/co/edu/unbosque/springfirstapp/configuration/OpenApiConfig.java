package co.edu.unbosque.springfirstapp.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI (Swagger) para documentar la API REST.
 * Esta clase define la información general de la API, esquemas de seguridad,
 * y componentes reutilizables para la documentación.
 */
@Configuration
public class OpenApiConfig {
    // Clase de configuración para la documentación OpenAPI

    /**
     * Define componentes reutilizables para la documentación OpenAPI.
     * Incluye ejemplos de respuestas comunes para mejorar la documentación.
     * 
     * @return Configuración personalizada de OpenAPI
     */
    @Bean
    public OpenAPI customOpenAPI() {
        // Crear la descripción principal con HTML correctamente formateado
        String mainDescription = "<h2>Guía para principiantes de la API REST</h2>" +
            "<p>Esta API proporciona funcionalidades para gestionar usuarios y autenticación mediante JWT.</p>" +
            "<h3>Conceptos básicos:</h3>" +
            "<ul>" +
            "    <li><strong>JWT (JSON Web Token)</strong>: Un estándar para crear tokens de acceso. Cuando inicias sesión, " +
            "        recibirás un token que debes incluir en las cabeceras de tus peticiones posteriores.</li>" +
            "    <li><strong>Autenticación</strong>: Proceso de verificar tu identidad mediante credenciales (usuario y contraseña).</li>" +
            "    <li><strong>Autorización</strong>: Determina qué acciones puede realizar un usuario autenticado según su rol.</li>" +
            "</ul>" +
            "<h3>Flujo básico de uso:</h3>" +
            "<ol>" +
            "    <li>Registra un nuevo usuario usando <code>/auth/register</code></li>" +
            "    <li>Inicia sesión con <code>/auth/login</code> para obtener un token JWT</li>" +
            "    <li>Incluye el token en el encabezado de autorización de tus peticiones: <code>Authorization: Bearer tu_token_jwt</code></li>" +
            "    <li>Usa los endpoints de usuario para gestionar usuarios (requiere autenticación)</li>" +
            "</ol>" +
            "<h3>Roles de usuario:</h3>" +
            "<ul>" +
            "    <li><strong>USER</strong>: Puede ver información de usuarios</li>" +
            "    <li><strong>ADMIN</strong>: Puede crear, actualizar y eliminar usuarios además de ver información</li>" +
            "</ul>" +
            "<h3>Códigos de estado HTTP comunes:</h3>" +
            "<ul>" +
            "    <li><strong>200/201</strong>: Operación exitosa</li>" +
            "    <li><strong>400</strong>: Error en la solicitud (datos incorrectos)</li>" +
            "    <li><strong>401</strong>: No autenticado (token inválido o expirado)</li>" +
            "    <li><strong>403</strong>: No autorizado (no tienes permisos suficientes)</li>" +
            "    <li><strong>404</strong>: Recurso no encontrado</li>" +
            "    <li><strong>409</strong>: Conflicto (por ejemplo, nombre de usuario ya existente)</li>" +
            "</ul>";

        // Crear la descripción del esquema de seguridad con HTML correctamente formateado
        String securityDescription = "Autenticación mediante JWT (JSON Web Token)." +
            "<p>Para autenticarte, sigue estos pasos:</p>" +
            "<ol>" +
            "    <li>Obtén un token JWT usando el endpoint <code>/auth/login</code></li>" +
            "    <li>Copia el token recibido en la respuesta</li>" +
            "    <li>Haz clic en el botón \"Authorize\" en la parte superior de esta página</li>" +
            "    <li>En el campo \"Value\", escribe: <code>Bearer tu_token_jwt</code></li>" +
            "    <li>Haz clic en \"Authorize\" y luego en \"Close\"</li>" +
            "</ol>" +
            "<p>Ahora podrás acceder a los endpoints protegidos.</p>";

        // Crear objeto Info con la descripción HTML
        io.swagger.v3.oas.models.info.Info info = new io.swagger.v3.oas.models.info.Info()
            .title("API de Primera Aplicación Spring")
            .version("1.0")
            .description(mainDescription)
            .contact(new io.swagger.v3.oas.models.info.Contact()
                .name("Equipo de Desarrollo")
                .email("soporte@ejemplo.com")
                .url("https://github.com/tu-usuario/SpringFirstAppJWT"))
            .license(new io.swagger.v3.oas.models.info.License()
                .name("Licencia MIT")
                .url("https://opensource.org/licenses/MIT"));

        // Crear el esquema de seguridad
        io.swagger.v3.oas.models.security.SecurityScheme securityScheme = new io.swagger.v3.oas.models.security.SecurityScheme()
            .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .description(securityDescription);

        return new OpenAPI()
            .info(info)
            .components(new Components()
                .addSecuritySchemes("bearerAuth", securityScheme)
                .addResponses("UnauthorizedError", new ApiResponse()
                    .description("No autenticado - Token JWT inválido o expirado")
                    .content(new Content().addMediaType("application/json", 
                        new MediaType().addExamples("error", new Example()
                            .value("{\"error\": \"No autorizado\", \"mensaje\": \"Token inválido o expirado\"}")))))
                .addResponses("ForbiddenError", new ApiResponse()
                    .description("Acceso prohibido - No tienes permisos suficientes")
                    .content(new Content().addMediaType("application/json", 
                        new MediaType().addExamples("error", new Example()
                            .value("{\"error\": \"Acceso prohibido\", \"mensaje\": \"No tienes permisos para esta operación\"}")))))
                .addResponses("NotFoundError", new ApiResponse()
                    .description("Recurso no encontrado")
                    .content(new Content().addMediaType("application/json", 
                        new MediaType().addExamples("error", new Example()
                            .value("{\"error\": \"No encontrado\", \"mensaje\": \"El recurso solicitado no existe\"}")))))
            );
    }
}
