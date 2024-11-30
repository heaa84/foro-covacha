package cobacha.foro.domain.curso;

import cobacha.foro.domain.topico.Topico;

import java.time.LocalDateTime;
import java.util.List;

public record DatosListadoCursos(
        Long id,
        String nombre,
        String categoria

) {
    // Constructor

    public DatosListadoCursos(Curso curso) {
        this(curso.getId(), curso.getNombre(), curso.getCategoria());
    }
}
