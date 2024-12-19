package covacha.foro.service.curso.validadores;

import covacha.foro.domain.curso.CursoRepository;
import covacha.foro.domain.curso.dto.DatosActualizarCurso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarExistenciaDeCurso implements ValidadorCurso {
    @Autowired
    private CursoRepository cursoRepository;
    public void validar(DatosActualizarCurso datos){
        if (cursoRepository.existsByNombreAndCategoria(datos.nombre(), datos.categoria())) {
            throw new RuntimeException("Error ya existe el curso");
        }

    }
}
