package covacha.foro.service.validadores;

import covacha.foro.domain.curso.dto.DatosActualizarCurso;
import covacha.foro.infra.errores.TratadorDeErrores;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ValidarActualizarCurso implements ValidadorCurso {
    public void validar(DatosActualizarCurso datos){
        String regex="^[a-zA-Z]{3,}$";
        if (!datos.nombre().matches(regex)){
            System.out.println(datos.nombre().matches(regex));
            throw new RuntimeException("Error en el nombre");
        }
        if (!datos.categoria().matches(regex)){
            throw new RuntimeException("Error en el nombre");
        }

    }
}
