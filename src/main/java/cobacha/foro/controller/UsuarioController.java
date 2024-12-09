package cobacha.foro.controller;

import cobacha.foro.domain.curso.*;
import cobacha.foro.domain.usuario.DatosListadoUsuario;
import cobacha.foro.domain.usuario.UsuarioRepository;
import cobacha.foro.infra.errores.TratadorDeErrores;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;


    // Listar todos los Cursos
    @GetMapping
    public ResponseEntity<Page<DatosListadoUsuario>> listadoUsuarios(@PageableDefault(size = 10 , sort = "id") Pageable paginacion) {
        return ResponseEntity.ok(usuarioRepository.findAll(paginacion).map(DatosListadoUsuario::new));
    }



/*
    // Actualizar tópico

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

 */

}
