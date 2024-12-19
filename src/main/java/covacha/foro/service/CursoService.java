package covacha.foro.service;

import covacha.foro.domain.curso.Curso;
import covacha.foro.domain.curso.CursoRepository;
import covacha.foro.domain.curso.dto.DatosActualizarCurso;
import covacha.foro.domain.curso.dto.DatosRepuestaCurso;
import covacha.foro.infra.errores.TratadorDeErrores;
import covacha.foro.service.validadores.ValidarActualizarCurso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private List<ValidarActualizarCurso> validarActualizarCursos;

    public ResponseEntity<?> actualizarCurso(Long id, DatosActualizarCurso datos){
        // Validadores
        validarActualizarCursos.forEach(v-> v.validar(datos));

        // Verificar si ya existe un Curso
        if (cursoRepository.existsByNombreAndCategoria(datos.nombre(), datos.categoria())) {
            TratadorDeErrores errorResponse = new TratadorDeErrores("Ya existe el Curso");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        Curso curso=cursoRepository.getReferenceById(id);
        curso.actualizarDatos(datos.nombre(), datos.categoria());
        var datosCurso= new DatosRepuestaCurso(
                curso.getId(),
                curso.getNombre(),
                curso.getCategoria()
        );
        return ResponseEntity.ok(datosCurso);
    }
}
