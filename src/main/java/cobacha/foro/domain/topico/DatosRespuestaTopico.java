package cobacha.foro.domain.topico;

import cobacha.foro.domain.curso.Curso;
import cobacha.foro.domain.curso.DatosRepuestaCurso;

import java.time.LocalDateTime;
import java.util.List;

public record DatosRespuestaTopico(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        TopicoStatus estado,
        String autor,
        List<DatosRepuestaCurso> curso
        ) {
}
