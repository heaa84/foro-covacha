package covacha.foro.service.topico.validadores;
import covacha.foro.domain.curso.CursoRepository;

import covacha.foro.domain.topico.TopicoRepository;
import covacha.foro.domain.topico.dto.DatosRegistroTopicoConCurso;
import covacha.foro.service.ValidacionExcepcion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidTopico implements InterfaceValid {

    @Autowired
    TopicoRepository topicoRepository;

    @Autowired
    CursoRepository cursoRepository;

    public void validar(DatosRegistroTopicoConCurso datos) {
        if ((topicoRepository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje()) && cursoRepository.existsByNombreAndCategoria(datos.nombre(), datos.categoria()))) {
            throw new ValidacionExcepcion("Ya existe un tópico con el mismo título y mensaje");
        }
    }
}
