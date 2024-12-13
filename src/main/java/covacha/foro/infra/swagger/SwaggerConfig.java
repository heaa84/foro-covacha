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
