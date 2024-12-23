package covacha.foro.service.curso;

import covacha.foro.domain.curso.Curso;
import covacha.foro.domain.curso.CursoRepository;
import covacha.foro.domain.curso.dto.DatosActualizarCurso;
import covacha.foro.domain.curso.dto.DatosRepuestaCurso;

import covacha.foro.service.curso.validadores.InterfaceValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private List<InterfaceValid> validadorCursos;


    public ResponseEntity<?> actualizarCurso(Long id, DatosActualizarCurso datos){
        // Validadores
        validadorCursos.forEach(v-> v.validar(datos));

        Curso curso=cursoRepository.getReferenceById(id);
        curso.actualizarDatos(datos);
        var datosCurso= new DatosRepuestaCurso(id,datos.nombre(), datos.categoria());
        return ResponseEntity.ok(datosCurso);
    }

    public Page<Curso.DatosListadoCursos> listarCursos(Pageable paginacion) {
        return cursoRepository.findAll(paginacion).map(Curso.DatosListadoCursos::new);
    }
}
