package covacha.foro.service.topico;
import covacha.foro.domain.curso.Curso;
import covacha.foro.domain.curso.CursoRepository;
import covacha.foro.domain.topico.Topico;
import covacha.foro.domain.topico.TopicoRepository;
import covacha.foro.domain.topico.dto.DatosActualizarTopico;
import covacha.foro.domain.topico.dto.DatosTopico;
import covacha.foro.domain.topico.dto.DatosRegistroTopicoConCurso;
import covacha.foro.domain.usuario.Usuario;
import covacha.foro.service.topico.validadores.InterfaceValidRegistro;
import covacha.foro.service.topico.validadores.InterfaceValidPorID;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
public class TopicoService {

    @Autowired
    private List<InterfaceValidRegistro> interfaceValidList;

    @Autowired
    private List<InterfaceValidPorID> interfaceValidPorIDS;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    public ResponseEntity<?> registrarTopicoConCurso(DatosRegistroTopicoConCurso datos, UriComponentsBuilder uriComponentsBuilder, Authentication authentication) {
        // validadores
        // Verificar si ya existe un tópico con el mismo título y mensaje
        interfaceValidList.forEach(v -> v.validar(datos)); // lógica ya validado los posibles errores

        //Asignar autor logueado al Topico
        Usuario usuario = (Usuario) authentication.getPrincipal();

        // creando curso si no existe en la bd con los datos enviados por insomnia
        Curso curso = cursoRepository.findByNombreAndCategoria(datos.nombre(), datos.categoria())
                .orElseGet(() -> {
                    // Si no existe, crear un nuevo curso
                    Curso nuevoCurso = new Curso();
                    nuevoCurso.setNombre(datos.nombre());
                    nuevoCurso.setCategoria(datos.categoria());
                    // Guardar el nuevo curso en la base de datos
                    return cursoRepository.save(nuevoCurso);
                });

        //crear topico y asociar al curso
        Topico topico = new Topico(datos);
        topico.setCurso(curso); // Asociar el curso al topico
        topico.setAutor(usuario.getNombre()); // Asociar El usuario al Autor

        // Guardar el tópico
        topicoRepository.save(topico);
        var datosTopico = new DatosTopico(topico);

        // Construir la URI para el recurso recién creado
        URI url = uriComponentsBuilder.path("/topico/{id}").buildAndExpand(topico.getId()).toUri();

        return ResponseEntity.created(url).body(datosTopico);
    }

    public ResponseEntity<?> buscarTopicoID(long id) {
        interfaceValidPorIDS.forEach(v -> v.validar(id));

        Topico topico = topicoRepository.getReferenceById(id);

        var datosTopico = new DatosTopico(topico);
        return ResponseEntity.ok(datosTopico);
    }

    public ResponseEntity<?> actualizarTopico(Long id, DatosActualizarTopico datos) {
        interfaceValidPorIDS.forEach(v -> v.validar(id));

        Optional<Topico> optionalTopico = topicoRepository.findById(id);
        if (optionalTopico.isPresent()) {
            Topico topico = topicoRepository.getReferenceById(id); // obtener datos del topico, que esta en BD y guardarlos en topico
            topico.actualizarDatos(datos); // enviar datos que queremos actualizar al metodo actualizarDatos
        /* Actualizar Curso. Importante no actualizar directamente el curso sino crear una
        instancia de curso nueva, para posteriormene acignarcela al topico
         */
            // Verificar si hay datos para actualizar en el curso
            if (datos.nombre() != null || datos.categoria() != null) {
                // Obtener los datos del curso actual del topico
                String nombre = (datos.nombre() != null ? datos.nombre() : topico.getCurso().getNombre());
                String categoria = (datos.categoria() != null ? datos.categoria() : topico.getCurso().getCategoria());

                // Verificar si ya existe un Curso con los mismos datos
                var cursoExistente = cursoRepository.findByNombreAndCategoria(nombre, categoria);

                if (cursoExistente.isPresent()) {// Si hay un curso existenete
                    // Asignar el curso existente al topico
                    topico.setCurso(cursoExistente.get());
                } else {
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

            var datosTopico = new DatosTopico(topico);
            return ResponseEntity.ok(datosTopico);
        }
        return ResponseEntity.badRequest().body("Topico no entrontrado para actualizar");
    }

    public ResponseEntity<?> eliminarTopico(Long id) {
        // Buscar el tópico a eliminar
        Optional<Topico> optionalTopico = topicoRepository.findById(id);
        if (optionalTopico.isPresent()) {
            Topico topico = optionalTopico.get();
            // Obtener el curso asociado
            Curso curso = topico.getCurso();
            // Remover el tópico de la lista del curso
            curso.getTopicos().remove(topico);
            System.out.println("Numero de topicos: " + curso.getTopicos().size());
            if (curso.getTopicos().size() < 1) {
                cursoRepository.delete(curso);
                System.out.println("se Elimino  el curso");
            }
        } else {
            return ResponseEntity.badRequest().body("Topico no encontrado o ya fue eliminado");
        }
        return ResponseEntity.ok("Tópico eliminado con éxito");
    }

    public Page<DatosTopico> listaTopico(Pageable paginacion) {
        return topicoRepository.findAll(paginacion).map(DatosTopico::new);
    }
}