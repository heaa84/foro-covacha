package covacha.foro.domain.topico.dto;


import covacha.foro.domain.respuesta.dto.DatosRespuesta;
import covacha.foro.domain.topico.TopicoStatus;

import java.time.LocalDateTime;
import java.util.List;


public record DatosRespuestaTopico(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        TopicoStatus estado,
        String autor,
        String nombre,
        String categoria,
        List<DatosRespuesta> respuestas // Nueva lista de respuestas
        ) {
}
