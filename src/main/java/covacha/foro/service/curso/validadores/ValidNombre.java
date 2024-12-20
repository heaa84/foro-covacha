package covacha.foro.service.curso.validadores;

import covacha.foro.domain.curso.dto.DatosActualizarCurso;
import covacha.foro.service.ValidacionExcepcion;
import org.springframework.stereotype.Component;

@Component
public class ValidNombre implements InterfaceValid {
    public void validar(DatosActualizarCurso datos){
        String regex="^[a-zA-Z]{3,}$";
        if (!datos.nombre().matches(regex)){
            throw new ValidacionExcepcion("Error en el nombre");
        }
        if (!datos.categoria().matches(regex)){
            throw new ValidacionExcepcion("Error en el nombre");
        }

    }
}
