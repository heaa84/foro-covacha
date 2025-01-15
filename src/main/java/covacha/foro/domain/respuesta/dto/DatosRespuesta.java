package covacha.foro.domain.respuesta.dto;

import covacha.foro.domain.respuesta.Respuesta;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record DatosRespuesta(
        Long id,
        String mensaje,
        String fechaCreacion,
        String usuarioQueRespondio
) {
    public DatosRespuesta(Respuesta respuesta) {
        this(
                respuesta.getId(),
                respuesta.getMensaje(),
                formatted(respuesta.getFechaCreacion()),
                respuesta.getUsuarioQueRespondio()
        );
    }

    private static String formatted(LocalDateTime fecha) {
        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("dd-MM-yyy  HH:mm:ss");
        return fecha.format(formatter);
    }
}




