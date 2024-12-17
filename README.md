# Foro Covacha API

Foro Covacha es una API REST desarrollada en Java con Spring Boot 3. Esta API permite gestionar un foro con autenticación y autorización mediante JWT y OAuth2. Usa Flyway para la gestión de migraciones de la base de datos y JPA para la persistencia. Además, incluye H2 como base de datos en memoria para facilitar el desarrollo y pruebas.

## Características

- **API RESTful** para interactuar con el foro.
- **Autenticación y autorización** con JWT y OAuth2.
- **Migraciones de base de datos** gestionadas con Flyway.
- **Persistencia de datos** mediante JPA.
- **Base de datos H2** en memoria para pruebas rápidas y desarrollo.
- **Usuarios por defecto**:
    - `admin` con rol `ADMIN`
    - `user` con rol `USER`
- **Swagger** para la documentación interactiva de la API.

## Tecnologías

- **Java 17+**
- **Spring Boot 3**
- **Flyway** para migraciones de base de datos
- **Lombok** para reducción de código boilerplate
- **JPA** con Hibernate para persistencia en base de datos
- **H2** como base de datos en memoria (para pruebas)
- **JWT y OAuth2** para seguridad y autenticación [README SECURITY.md](README%20SECURITY.md)
- **Swagger** para documentación de la API´ [DOC SWAGGER.md](DOC%20SWAGGER.md)

## Configuración

### Propiedades de configuración

```properties
# spring.profiles.active=dev // Base de datos
# spring.profiles.active=h2 // Base de datos en memoria

spring.profiles.active=h2
```
# Roles usuarios: 
![Roles Usuarios.png](src%2Fmain%2Fresources%2Fimg%2FRoles%20Usuarios.png)

# ESQUEMAS DE TABLAS:
![tablas.png](src%2Fmain%2Fresources%2Fimg%2Ftablas.png)
![vista_tablas_qsl.png](src%2Fmain%2Fresources%2Fimg%2Fvista_tablas_qsl.png)