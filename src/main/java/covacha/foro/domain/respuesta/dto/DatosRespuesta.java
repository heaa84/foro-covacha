package covacha.foro.domain.respuesta.dto;

import covacha.foro.domain.respuesta.Respuesta;

import java.time.LocalDateTime;

public record DatosRespuesta(
        Long id,
        String mensaje,
        LocalDateTime fechaCreacion,
        String usuarioQueRespondio
) {
    public DatosRespuesta(Respuesta respuesta) {
        this(
                respuesta.getId(),
                respuesta.getMensaje(),
                respuesta.getFechaCreacion(),
                respuesta.getUsuarioQueRespondio()
        );
    }
}




