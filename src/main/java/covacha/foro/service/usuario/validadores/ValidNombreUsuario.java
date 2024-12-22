package covacha.foro.service.usuario.validadores;

import covacha.foro.domain.usuario.dto.DatosUsuario;
import covacha.foro.service.ValidacionExcepcion;
import covacha.foro.service.usuario.validadores.InterfaceValid;
import org.springframework.stereotype.Component;

@Component
public class ValidNombreUsuario implements InterfaceValid {
    @Override
    public void validar(DatosUsuario datos) {
        if(datos.nombre().length()<4){
            throw new ValidacionExcepcion("Error mensaje muy corto o nulo");
        }
    }
}
