package covacha.foro.service.respuesta.validadores;

import covacha.foro.domain.respuesta.dto.DatosRegistrarRespuesta;
import covacha.foro.service.ValidacionExcepcion;
import org.springframework.stereotype.Component;

@Component
public class ValidMensaje implements InterfaceValid {
    @Override
    public void validar(DatosRegistrarRespuesta datos) {
        if(datos.mensaje().length()<4){
            throw new ValidacionExcepcion("Error mensaje muy corto o nulo");
        }
    }
}
