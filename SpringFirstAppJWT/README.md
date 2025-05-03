# Spring First App con Autenticación JWT

## Descripción General
Esta es una aplicación Spring Boot que demuestra la autenticación y autorización mediante JWT (JSON Web Token). Proporciona una API REST segura con capacidades de gestión de usuarios.

## Características
- Autenticación de usuarios con JWT
- Autorización basada en roles (roles USER y ADMIN)
- Gestión de usuarios (operaciones CRUD)
- Encriptación de contraseñas
- API RESTful con documentación Swagger
- Manejo de excepciones

## Tecnologías
- Java 21
- Spring Boot 3.3.4
- Spring Security
- Spring Data JPA
- Base de datos MySQL
- JWT (JSON Web Token)
- Maven
- Swagger/OpenAPI

## Requisitos Previos
- Java 21 o superior
- Maven
- Base de datos MySQL

## Configuración e Instalación
1. Clonar el repositorio
2. Configurar la conexión a la base de datos en `application.properties`
3. Ejecutar la aplicación usando Maven:
   ```
   mvn spring-boot:run
   ```

## Documentación de la API
La documentación de la API está disponible a través de Swagger UI en:
```
http://localhost:8080/swagger-ui.html
```

## Autenticación
Para autenticarse, envíe una solicitud POST a `/auth/login` con nombre de usuario y contraseña. La respuesta incluirá un token JWT que debe incluirse en el encabezado de Autorización para solicitudes posteriores:
```
Authorization: Bearer {token}
```

## Gestión de Usuarios
La aplicación proporciona los siguientes endpoints para la gestión de usuarios:
- `POST /user/create` - Crear un nuevo usuario (requiere rol ADMIN)
- `GET /user/getall` - Obtener todos los usuarios (requiere rol USER o ADMIN)
- `GET /user/getbyid/{id}` - Obtener usuario por ID (requiere rol USER o ADMIN)
- `PUT /user/update` - Actualizar usuario (requiere rol ADMIN)
- `DELETE /user/deletebyid/{id}` - Eliminar usuario por ID (requiere rol ADMIN)
- `DELETE /user/deletebyname` - Eliminar usuario por nombre de usuario (requiere rol ADMIN)

## Testing
El proyecto incluye pruebas unitarias para las siguientes clases:

- **SpringFirstAppApplication**: Pruebas para verificar que el contexto de la aplicación se carga correctamente y que el bean ModelMapper se crea correctamente.
- **ServletInitializer**: Pruebas para verificar que el inicializador de Servlet configura correctamente la aplicación.
- **PasswordNotValidException**: Pruebas para verificar que la excepción se crea con el mensaje de error adecuado.
- **UserController**: Pruebas para verificar que el controlador de usuarios maneja correctamente las solicitudes HTTP.
- **JwtUtil**: Pruebas para verificar que la generación y validación de tokens JWT funciona correctamente.
- **AuthController**: Pruebas para verificar que la autenticación de usuarios funciona correctamente.

Para ejecutar las pruebas, utilice Maven:
```
mvn test
```

## Javadoc
La documentación Javadoc está disponible en español. Para generar la documentación Javadoc, utilice Maven:
```
mvn javadoc:javadoc
```
La documentación generada estará disponible en el directorio `target/site/apidocs`.


## Licencia
Este proyecto está licenciado bajo la Licencia MIT.

## Colaboradores
- Universidad El Bosque
