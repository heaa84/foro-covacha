package covacha.foro.service.respuesta;

import covacha.foro.domain.respuesta.dto.DatosRegistrarRespuesta;

public interface ValidadorRespuesta {
    void validar(DatosRegistrarRespuesta datos);
}
