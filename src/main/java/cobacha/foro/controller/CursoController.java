package cobacha.foro.controller;

import cobacha.foro.domain.curso.*;
import cobacha.foro.domain.topico.*;
import cobacha.foro.infra.errores.TratadorDeErrores;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN') , hasRole('USER')")
    @GetMapping
    public ResponseEntity<Page<DatosListadoCursos>> listadoTopicos(@PageableDefault(size = 10 , sort = "id") Pageable paginacion) {
        return ResponseEntity.ok(cursoRepository.findAll(paginacion).map(DatosListadoCursos::new));
    }



    // Actualizar tópico
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity  actualizarCurso (@PathVariable Long id,@RequestBody @Valid DatosActualizarCurso datosActualizarCurso){
        // Verificar si ya existe un Curso
        if (cursoRepository.existsByNombreAndCategoria(datosActualizarCurso.nombre(), datosActualizarCurso.categoria())) {
            TratadorDeErrores errorResponse = new TratadorDeErrores("Ya existe el Curso");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        Curso curso=cursoRepository.getReferenceById(id);
        curso.actualizarDatos(datosActualizarCurso.nombre(), datosActualizarCurso.categoria());
        var datosCurso= new DatosRepuestaCurso(
                curso.getId(),
                curso.getNombre(),
                curso.getCategoria()
        );
        return ResponseEntity.ok(datosCurso);
    }

}
