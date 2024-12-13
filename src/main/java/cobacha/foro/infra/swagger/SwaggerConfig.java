package cobacha.foro.infra.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI(@Value("${app.version:0.0.1-SNAPSHOT}") String appVersion) {
        return new OpenAPI()
                .info(new Info()
                        .title("Foro Cobacha")
                        .version(appVersion)
                        .description("API RES de un foro de sobre programacion ....")
                );
    }
}
