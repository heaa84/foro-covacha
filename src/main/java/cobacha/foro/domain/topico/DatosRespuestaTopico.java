package cobacha.foro.domain.topico;

import cobacha.foro.domain.curso.Curso;

import java.time.LocalDateTime;

public record DatosRespuestaTopico(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        TopicoStatus estado,
        String autor
        ) {
}
