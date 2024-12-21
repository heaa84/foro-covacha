package covacha.foro.controller;
import covacha.foro.domain.curso.Curso;
import covacha.foro.domain.curso.CursoRepository;
import covacha.foro.domain.respuesta.dto.DatosRespuesta;
import covacha.foro.domain.topico.Topico;
import covacha.foro.domain.topico.TopicoRepository;
import covacha.foro.domain.topico.dto.DatosActualizarTopico;
import covacha.foro.domain.topico.dto.DatosTopico;
import covacha.foro.domain.topico.dto.DatosRegistroTopicoConCurso;
import covacha.foro.service.topico.TopicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;


@RestController
@RequestMapping("/topico")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private TopicoService topicoService;



    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping
    @Transactional
    @Operation(
            summary = "Registra un tópico",
            description = "Registramos un tópico")
    public ResponseEntity registrarTopicoConCurso(
            @RequestBody @Valid DatosRegistroTopicoConCurso datosRegistroTopicoConCurso,
            UriComponentsBuilder uriComponentsBuilder,
            Authentication authentication) {
        return ResponseEntity.ok(topicoService.registrarTopicoConCurso(datosRegistroTopicoConCurso,uriComponentsBuilder,authentication));
    }


    // Listar todos los tópicos
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping
    @Operation(
            summary = "Obtiene la lista de tópicos",
            description = "Retorna todos los tópicos sin requerir parámetros de entrada")
    public ResponseEntity<Page<DatosTopico>> listadoTopicos(
            @Parameter(hidden = true) // Ocultar parámetros para evitar que swagger los pida
            @PageableDefault(size = 10 , sort = "id") Pageable paginacion) {

            var topicos = topicoRepository.findAll(paginacion)
                .map(DatosTopico::new);
        return ResponseEntity.ok(topicos);
    }

    //Topico por ID
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/{id}")
    @Transactional
    @Operation(
            summary = "Obtener tópico por id",
            description = "Devuelve el tópico seleccionado por id ")
    public ResponseEntity <?> topicoPorId (@PathVariable Long id){
            return ResponseEntity.badRequest().body("Topico con existe Revisar id");
    }

    // Actualizar tópico
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Transactional
    @Operation(
            summary = "Actualizar tópico",
            description = "Solo ADMIN puede actualizar un tópico")
    public ResponseEntity<?>  actualizarTopico (@PathVariable Long id,@RequestBody @Valid DatosActualizarTopico datosActualizarTopico){
        Optional<Topico> optionalTopico=topicoRepository.findById(id);
        if (optionalTopico.isPresent()){
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
            // Mapear las respuestas del tópico a la lista de DatosRespuesta
            var respuestas = topico.getRespuestas().stream()
                    .map(respuesta -> new DatosRespuesta(
                            respuesta.getId(),
                            respuesta.getMensaje(),
                            respuesta.getFechaCreacion(),
                            respuesta.getUsuarioQueRespondio()
                    ))
                    .toList();

            var datosTopico = new DatosTopico(topico);
            return ResponseEntity.ok(datosTopico);
        }
        return ResponseEntity.badRequest().body("Topico no entrontrado para actualizar");
    }

    // Eliminar Topico de la BD
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Transactional
    @Operation(
            summary = "Eliminar tópico",
            description = "Solo un ADMIN puede eliminar un tópico")
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
