package covacha.foro.domain.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record DatosAtuentificacionUsuario(
        @Schema(example = "admin")
        String nombre,
        @Schema(example = "admin")
        String contrasena
) {
}
