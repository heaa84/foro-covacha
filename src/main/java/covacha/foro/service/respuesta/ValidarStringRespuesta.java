package covacha.foro.service.respuesta;

import covacha.foro.domain.respuesta.dto.DatosRegistrarRespuesta;
import covacha.foro.service.ValidacionExcepcion;
import org.springframework.stereotype.Component;

@Component
public class ValidarStringRespuesta implements ValidadorRespuesta{
    @Override
    public void validar(DatosRegistrarRespuesta datos) {
        if(datos.mensaje().length()<4){
            throw new ValidacionExcepcion("Error mensaje muy corto o nulo");
        }
    }
}
