package covacha.foro.domain.curso.dto;

import covacha.foro.domain.curso.Curso;

public record DatosActualizarCurso(
        String nombre ,
        String categoria) {
    public DatosActualizarCurso (Curso curso){
        this(curso.getNombre(), curso.getCategoria());
    }
}
