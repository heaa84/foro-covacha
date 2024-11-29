package cobacha.foro.controller;

import cobacha.foro.domain.curso.CursoRepository;
import cobacha.foro.domain.curso.DatosRepuestaCurso;
import cobacha.foro.domain.topico.*;
import cobacha.foro.infra.errores.TratadorDeErrores;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/topico")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private CursoRepository cursoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity registrarTopicoConCurso(
            @RequestBody @Valid DatosRegistroTopicoConCurso datosRegistroTopicoConCurso,
            UriComponentsBuilder uriComponentsBuilder) {

        // Verificar si ya existe un tópico con el mismo título y mensaje
        if (topicoRepository.existsByTituloAndMensaje(datosRegistroTopicoConCurso.titulo(), datosRegistroTopicoConCurso.mensaje())) {
            TratadorDeErrores errorResponse = new TratadorDeErrores("Ya existe un tópico con el mismo título y mensaje");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // Crear el tópico y asociarlo al curso
        Topico topico = new Topico(datosRegistroTopicoConCurso);

        // Guardar el tópico
        topicoRepository.save(topico);
        // Crea una lista con y dentro un objecto Curso y se le pasa los parametros nobre y categoria
        List<DatosRepuestaCurso> cursosRespuesta = topico.getCurso().stream()
                .map(curso -> new DatosRepuestaCurso(curso.getNombre(), curso.getCategoria()))
                .collect(Collectors.toList());

        // Crear el objeto de respuesta
        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getStatus(),
                topico.getAutor(),
                cursosRespuesta// objeto curso
        );

        // Construir la URI para el recurso recién creado
        URI url = uriComponentsBuilder.path("/topico/{id}").buildAndExpand(topico.getId()).toUri();

        // Devolver la respuesta con estado CREATED
        return ResponseEntity.created(url).body(datosRespuestaTopico);
    }



    // Listar todos los tópicos
    @GetMapping
    public ResponseEntity<Page<DatosListadoTopico>> listadoTopicos(@PageableDefault(size = 10 , sort = "fechaCreacion") Pageable paginacion) {
        return ResponseEntity.ok(topicoRepository.findAll(paginacion).map(DatosListadoTopico::new));
    }
    //Topico por ID
    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity <DatosRespuestaTopico> topicoPorId (@PathVariable Long id){
        Topico topico=topicoRepository.getReferenceById(id);

        List<DatosRepuestaCurso> cursosRespuesta = topico.getCurso().stream()
                .map(curso -> new DatosRepuestaCurso(curso.getNombre(), curso.getCategoria()))
                .collect(Collectors.toList());

        var datosTopico = new DatosRespuestaTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getStatus(),
                topico.getAutor(),
                cursosRespuesta
        );
        return ResponseEntity.ok(datosTopico);
    }

    // Actualizar tópico

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity  actualizarTopico (@PathVariable Long id,@RequestBody @Valid DatosActualizarTopico datosActualizarTopico){
        Topico topico=topicoRepository.getReferenceById(id); // obtener datos del topico, que esta en BD y guardarlos en topico
        topico.actualizarDatos(datosActualizarTopico); // enviar datos que queremos actualizar al metodo actualizarDatos

        return ResponseEntity.ok(id);
    }

}
