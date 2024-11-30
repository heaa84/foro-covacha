package cobacha.foro.controller;

import cobacha.foro.domain.curso.Curso;
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
import java.util.ArrayList;
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
        if ((topicoRepository.existsByTituloAndMensaje(datosRegistroTopicoConCurso.titulo(), datosRegistroTopicoConCurso.mensaje()) && cursoRepository.existsByNombreAndCategoria(datosRegistroTopicoConCurso.nombre(), datosRegistroTopicoConCurso.categoria()))) {
            TratadorDeErrores errorResponse = new TratadorDeErrores("Ya existe un tópico con el mismo título y mensaje");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // creando curso si no existe en la bd con los datos enviados por insomnia
        Curso curso = cursoRepository.findByNombreAndCategoria(datosRegistroTopicoConCurso.nombre(), datosRegistroTopicoConCurso.categoria())
                .orElseGet(() -> {
                    // Si no existe, crear un nuevo curso
                    Curso nuevoCurso = new Curso();
                    nuevoCurso.setNombre(datosRegistroTopicoConCurso.nombre());
                    nuevoCurso.setCategoria(datosRegistroTopicoConCurso.categoria());
                    // Guardar el nuevo curso en la base de datos
                    return cursoRepository.save(nuevoCurso);
                });
        //crear topico y asociar al curso
        Topico topico = new Topico(datosRegistroTopicoConCurso);
        topico.setCurso(curso); // Asociar el curso al topico

        // Guardar el tópico
        topicoRepository.save(topico);

        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getStatus(),
                topico.getAutor(),
                curso.getNombre(),
                curso.getCategoria()
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
    /*
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

        Curso cursoActualizar =cursoRepository.findByTopicoId(id);
        cursoActualizar.actualizarDatos(datosActualizarTopico.nombreCurso(), datosActualizarTopico.categoriaCurso());

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
*/
}
