package covacha.foro.service.curso.validadores;

import covacha.foro.domain.curso.dto.DatosActualizarCurso;


public interface ValidadorCurso {
    void validar(DatosActualizarCurso datos);
}
