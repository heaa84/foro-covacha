package cobacha.foro.controller;

import cobacha.foro.domain.curso.Curso;
import cobacha.foro.domain.curso.CursoRepository;
import cobacha.foro.domain.curso.DatosListadoCursos;
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
@RequestMapping("/curso")
public class CursoController {

    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private CursoRepository cursoRepository;


    // Listar todos los Cursos
    @GetMapping
    public ResponseEntity<Page<DatosListadoCursos>> listadoTopicos(@PageableDefault(size = 10 , sort = "id") Pageable paginacion) {
        return ResponseEntity.ok(cursoRepository.findAll(paginacion).map(DatosListadoCursos::new));
    }

/*
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
    */

}
