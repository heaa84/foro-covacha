package covacha.foro.controller;


import covacha.foro.domain.curso.Curso;
import covacha.foro.domain.curso.CursoRepository;
import covacha.foro.domain.curso.dto.DatosActualizarCurso;
import covacha.foro.domain.curso.dto.DatosRepuestaCurso;
import covacha.foro.domain.topico.TopicoRepository;
import covacha.foro.infra.errores.TratadorDeErrores;
import covacha.foro.service.CursoService;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/curso")
@SecurityRequirement(name = "bearer-key")
public class CursoController {

    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private CursoService cursoService;


    // Listar todos los Cursos
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping
    @Operation(
            summary = "Obtiene la lista de Cursos",
            description = "Retorna todos los cursos de la BD")
    public ResponseEntity<Page<Curso.DatosListadoCursos>> listadoTopicos(
            @Parameter(hidden = true)
            @PageableDefault(size = 10 , sort = "id") Pageable paginacion) {
        return ResponseEntity.ok(cursoRepository.findAll(paginacion).map(Curso.DatosListadoCursos::new));
    }

    // Actualizar tópico
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Transactional
    @Operation(
            summary = "Actualiza curso",
            description = "Solo ADMIN puede actualizar cursos de la BD")
    public ResponseEntity  actualizarCurso (@PathVariable Long id,@RequestBody @Valid DatosActualizarCurso datosActualizarCurso){
        return ResponseEntity.ok(cursoService.actualizarCurso(id, datosActualizarCurso));
    }

}
