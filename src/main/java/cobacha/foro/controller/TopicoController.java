package cobacha.foro.controller;

import cobacha.foro.domain.curso.Curso;
import cobacha.foro.domain.curso.CursoRepository;

import cobacha.foro.domain.topico.*;
import cobacha.foro.infra.errores.TratadorDeErrores;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.transaction.TransactionalException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;


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

    //Topico por ID
    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity <DatosRespuestaTopico> topicoPorId (@PathVariable Long id){
        Topico topico=topicoRepository.getReferenceById(id);


        var datosTopico = new DatosRespuestaTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getStatus(),
                topico.getAutor(),
                topico.getCurso().getNombre(),
                topico.getCurso().getCategoria()
        );
        return ResponseEntity.ok(datosTopico);
    }

    // Actualizar tópico

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity  actualizarTopico (@PathVariable Long id,@RequestBody @Valid DatosActualizarTopico datosActualizarTopico){
        Topico topico=topicoRepository.getReferenceById(id); // obtener datos del topico, que esta en BD y guardarlos en topico
        topico.actualizarDatos(datosActualizarTopico); // enviar datos que queremos actualizar al metodo actualizarDatos
        /* Actualizar Curso. Importante no actualizar directamente el curso sino crear una
        instancia de curso nueva, para posteriormene acignarcela al topico
         */
        // Verificar si hay datos para actualizar en el curso
        if(datosActualizarTopico.nombre() != null || datosActualizarTopico.categoria() !=null){
            // Obtener los datos del curso actual del topico
            String nombre=(datosActualizarTopico.nombre() != null ? datosActualizarTopico.nombre() : topico.getCurso().getNombre());
            String categoria=(datosActualizarTopico.categoria() != null ? datosActualizarTopico.categoria() : topico.getCurso().getCategoria());

            // Verificar si ya existe un Curso con los mismos datos
            var cursoExistente=cursoRepository.findByNombreAndCategoria(nombre,categoria);

            if (cursoExistente.isPresent()){// Si hay un curso existenete
                // Asignar el curso existente al topico
                topico.setCurso(cursoExistente.get());
            }else {
                // si no hay curso exitente crear un curso y asignar topico al curso
                Curso nuevoCurso = new Curso();
                nuevoCurso.setNombre(nombre);
                nuevoCurso.setCategoria(categoria);
                // Guardar el nuevo curso en BD
                cursoRepository.save(nuevoCurso);
                // Asignar El nuevo curso al topico
                topico.setCurso(nuevoCurso);
            }
        }

        var datosTopico = new DatosRespuestaTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getStatus(),
                topico.getAutor(),
                topico.getCurso().getNombre(),
                topico.getCurso().getCategoria()
        );
        return ResponseEntity.ok(datosTopico);
    }

    // Eliminar Topico de la BD
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> eliminarTopico(@PathVariable Long id) {
        // Buscar el tópico a eliminar
        Optional<Topico> optionalTopico = topicoRepository.findById(id);
        if(optionalTopico.isPresent()){
            Topico topico=optionalTopico.get();
            // Obtener el curso asociado
            Curso curso = topico.getCurso();
            // Remover el tópico de la lista del curso
            curso.getTopicos().remove(topico);
            System.out.println("Numero de topicos: "+ curso.getTopicos().size());
            if (curso.getTopicos().size()<1){
                cursoRepository.delete(curso);
                System.out.println("se Elimino  el curso");
            }
        }else {
            return ResponseEntity.badRequest().body("Topico no encontrado o ya fue eliminado");
        }

        return ResponseEntity.ok("Tópico eliminado con éxito");
    }

}
