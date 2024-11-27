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
        // Crear el objeto de respuesta para el curso


        // Guardar el tópico
        topicoRepository.save(topico);


        // Crear el objeto de respuesta
        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getStatus(),
                topico.getAutor()
        );

        // Construir la URI para el recurso recién creado
        URI url = uriComponentsBuilder.path("/topico/{id}").buildAndExpand(topico.getId()).toUri();

        // Devolver la respuesta con estado CREATED
        return ResponseEntity.created(url).body(datosRespuestaTopico);
    }

/*

    // Listar todos los tópicos
    @GetMapping
    public ResponseEntity<Page<DatosListadoTopico>> listadoTopicos(@PageableDefault(size = 2) Pageable paginacion) {
        return ResponseEntity.ok(topicoRepository.findAll(paginacion).map(DatosListadoTopico::new));
    }

    // Actualizar tópico
    @PutMapping
    @Transactional
    public ResponseEntity actualizarTopico(@RequestBody @Valid DatosActualizarTopico datosActualizarTopico) {
        Topico topico = topicoRepository.getReferenceById(datosActualizarTopico.id());
        topico.actualizarDatos(datosActualizarTopico);
        return ResponseEntity.ok(new DatosRespuestaTopico(topico.getId(), topico.getAutor(), topico.getTitulo(), topico.getMensaje(), topico.getFechaCreacion()));
    }
    */
}
