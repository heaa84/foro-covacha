package cobacha.foro.domain.topico;

import java.time.LocalDateTime;

public record DatosRespuestaTopico(
        Long id,
        String autor,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion
        //String autorNombre
) {
}
