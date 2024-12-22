package covacha.foro.service.usuario.validadores;

import covacha.foro.domain.usuario.dto.DatosUsuario;
import covacha.foro.service.ValidacionExcepcion;
import covacha.foro.service.respuesta.validadores.InterfaceValid;
import org.springframework.stereotype.Component;

@Component
public class ValidPerfilUsuario implements InterfaceValid {
    @Override
    public void validar(DatosUsuario datos) {
        if(datos.contrasena()!="ADMIN" && datos.contrasena()!="USER"){
            throw new ValidacionExcepcion("Error en el campo de Perfil");
        }
    }
}
