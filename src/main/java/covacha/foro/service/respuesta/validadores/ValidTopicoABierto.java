package covacha.foro.service.respuesta.validadores;

import covacha.foro.domain.respuesta.dto.DatosRegistrarRespuesta;
import covacha.foro.domain.topico.Topico;
import covacha.foro.domain.topico.TopicoRepository;
import covacha.foro.domain.topico.TopicoStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidTopicoABierto implements InterfaceValid {
    @Autowired
    private TopicoRepository topicoRepository;
    @Override
    public void validar(DatosRegistrarRespuesta datos) {
        Topico topico = topicoRepository.findById(datos.idTopico())
                .orElseThrow(() -> new IllegalArgumentException(
                        "El tópico con ID " + datos.idTopico() + " no existe."));

        if (topico.getStatus() == TopicoStatus.RESUELTO) {
            throw new IllegalStateException("El tópico ya está marcado como resuelto.");
        }
    }
}
