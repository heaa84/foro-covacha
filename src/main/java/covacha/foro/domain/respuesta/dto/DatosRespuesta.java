package covacha.foro.domain.respuesta.dto;

import java.time.LocalDateTime;

public record DatosRespuesta(
        Long id,
        String mensaje,
        LocalDateTime fechaCreacion,
        String usuarioQueRespondio
) {
}
