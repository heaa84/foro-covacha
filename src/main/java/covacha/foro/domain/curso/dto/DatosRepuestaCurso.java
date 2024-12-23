package covacha.foro.domain.curso.dto;

import covacha.foro.domain.curso.Curso;

public record DatosRepuestaCurso(long id, String Nombre, String categoria) {
    public DatosRepuestaCurso (Curso curso){
        this(
                curso.getId(),
                curso.getNombre(),
                curso.getCategoria()
        );
    }
}
