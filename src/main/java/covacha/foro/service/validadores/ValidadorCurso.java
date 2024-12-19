package covacha.foro.service.validadores;

import covacha.foro.domain.curso.dto.DatosActualizarCurso;
import org.springframework.http.ResponseEntity;

public interface ValidadorCurso {
    void validar(DatosActualizarCurso datos);
}
