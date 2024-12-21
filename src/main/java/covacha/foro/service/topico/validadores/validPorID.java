package covacha.foro.service.topico.validadores;

import covacha.foro.domain.topico.TopicoRepository;
import covacha.foro.service.ValidacionExcepcion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class validPorID implements InterfaceValidPorID {

    @Autowired
    TopicoRepository topicoRepository;

    public void validar(long id) {
        if (!topicoRepository.existsById(id)) {
            throw new ValidacionExcepcion("El tópico no existe");
        }
    }
}
