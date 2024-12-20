package covacha.foro.service.topico;

import covacha.foro.domain.curso.Curso;
import covacha.foro.domain.curso.CursoRepository;
import covacha.foro.domain.respuesta.dto.DatosRespuesta;
import covacha.foro.domain.topico.Topico;
import covacha.foro.domain.topico.TopicoRepository;
import covacha.foro.domain.topico.dto.DatosRegistroTopicoConCurso;
import covacha.foro.domain.topico.dto.DatosRespuestaTopico;
import covacha.foro.domain.usuario.Usuario;
import covacha.foro.infra.errores.TratadorDeErrores;
import covacha.foro.service.topico.validadores.InterfaceValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
public class TopicoService {

    @Autowired
    private List<InterfaceValid> interfaceValidList;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    public ResponseEntity<?> registrarTopicoConCurso(DatosRegistroTopicoConCurso datos, UriComponentsBuilder uriComponentsBuilder, Authentication authentication){
        // valdadores
        //interfaceValidList.forEach(v-> v.validar(datos));
        // lógica ya validado los posibles errores

        //Asignar autor logueado al Topico
        Usuario usuario=(Usuario) authentication.getPrincipal();

        // Verificar si ya existe un tópico con el mismo título y mensaje
        if ((topicoRepository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje()) && cursoRepository.existsByNombreAndCategoria(datos.nombre(), datos.categoria()))) {
            TratadorDeErrores errorResponse = new TratadorDeErrores("Ya existe un tópico con el mismo título y mensaje");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // creando curso si no existe en la bd con los datos enviados por insomnia
        Curso curso = cursoRepository.findByNombreAndCategoria(datos.nombre(), datos.categoria())
                .orElseGet(() -> {
                    // Si no existe, crear un nuevo curso
                    Curso nuevoCurso = new Curso();
                    nuevoCurso.setNombre(datos.nombre());
                    nuevoCurso.setCategoria(datos.categoria());
                    // Guardar el nuevo curso en la base de datos
                    return cursoRepository.save(nuevoCurso);
                });

        //crear topico y asociar al curso
        Topico topico = new Topico(datos);
        topico.setCurso(curso); // Asociar el curso al topico
        topico.setAutor(usuario.getNombre()); // Asociar El usuario al Autor

        // Guardar el tópico
        topicoRepository.save(topico);

        // Mapear las respuestas del tópico a la lista de DatosRespuesta
        var respuestas = topico.getRespuestas().stream()
                .map(respuesta -> new DatosRespuesta(
                        respuesta.getId(),
                        respuesta.getMensaje(),
                        respuesta.getFechaCreacion(),
                        respuesta.getUsuarioQueRespondio()
                ))
                .toList();

        var datosTopico = new DatosRespuestaTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getStatus(),
                topico.getAutor(),
                topico.getCurso().getNombre(),
                topico.getCurso().getCategoria(),
                respuestas // Agregamos las respuestas al DTO
        );

        // Construir la URI para el recurso recién creado
        URI url = uriComponentsBuilder.path("/topico/{id}").buildAndExpand(topico.getId()).toUri();

        return ResponseEntity.created(url).body(datos);
    }
}
