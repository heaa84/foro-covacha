package covacha.foro.service.validadores;

import covacha.foro.domain.curso.dto.DatosActualizarCurso;
import covacha.foro.service.ValidacionExcepcion;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ValidarNombreCurso implements ValidadorCurso {
    public void validar(DatosActualizarCurso datos){
        String regex="^[a-zA-Z]{3,}$";
        if (!datos.nombre().matches(regex)){
            ResponseEntity.badRequest().body("Error en el nombre");
            throw new ValidacionExcepcion("Error en el nombre");
        }
        if (!datos.categoria().matches(regex)){
            throw new ValidacionExcepcion("Error en el nombre");
        }

    }
}
