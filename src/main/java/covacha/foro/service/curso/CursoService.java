package covacha.foro.service.curso;

import covacha.foro.domain.curso.Curso;
import covacha.foro.domain.curso.CursoRepository;
import covacha.foro.domain.curso.dto.DatosActualizarCurso;
import covacha.foro.domain.curso.dto.DatosRepuestaCurso;

import covacha.foro.domain.respuesta.dto.DatosRespuesta;
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


    public Page<DatosRepuestaCurso> listarCursos(Pageable paginacion) {
        return cursoRepository.findAll(paginacion).map(DatosRepuestaCurso::new);
    }

    public DatosRepuestaCurso actualizarCurso(Long id, DatosActualizarCurso datos){
        // Validadores
        validadorCursos.forEach(v-> v.validar(datos));

        Curso curso=cursoRepository.getReferenceById(id);
        curso.actualizarDatos(datos);
        return new DatosRepuestaCurso(id,datos.nombre(), datos.categoria());
    }
}
