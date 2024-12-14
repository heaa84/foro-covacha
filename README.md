# Documentación: Swagger-ui

1. Creamos clase de configuración swagger:
```java
/*Clase para configurar la seguridad en swagger y la información principal que va a mostrar swagger*/
package covacha.foro.infra.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI(@Value("${app.version:0.0.1-SNAPSHOT}") String appVersion) {
        return new OpenAPI()
                // Conf seguridad
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                // config info
                .info(new Info()
                        .title("Foro Covacha")
                        .version(appVersion)
                        .description("API RES de un foro de sobre programación ....")
                );
    }
}

```
2. Notaciones para la implementación swagger en los controller (Ejemplo):
```java    
// Listar todos los Usuarios
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping
    // Configuramos lo que va a mostrar swagger de este método
    @Operation(
            summary = "Obtiene la lista de usuarios",
            description = "Retorna todos los usuarios sin requerir parámetros de entrada")
    public ResponseEntity<Page<DatosListadoUsuario>> listadoUsuarios(
            @Parameter(hidden = true) // Ocultar parámetros para evitar que swagger los pida            
            @PageableDefault(size = 10 , sort = "id") Pageable paginacion) {
        return ResponseEntity.ok(usuarioRepository.findAll(paginacion).map(DatosListadoUsuario::new));
    }
```
3. URL de la cocumentacion:
http://localhost:8080/swagger-ui/index.html