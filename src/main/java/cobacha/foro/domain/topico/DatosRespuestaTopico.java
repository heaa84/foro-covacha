package cobacha.foro.domain.topico;


import java.time.LocalDateTime;


public record DatosRespuestaTopico(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        TopicoStatus estado,
        String autor,
        String nombre,
        String categoria
        ) {
}
