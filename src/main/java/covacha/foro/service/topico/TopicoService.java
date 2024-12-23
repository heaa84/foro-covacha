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

    public DatosTopico buscarTopicoID(long id) {
        interfaceValidPorIDS.forEach(v -> v.validar(id));
        Topico topico = topicoRepository.getReferenceById(id);
        return new DatosTopico(topico);
    }

    public DatosActualizarTopico actualizarTopico(Long id, DatosActualizarTopico datos, Authentication authentication) {
        interfaceValidPorIDS.forEach(v -> v.validar(id));
        // obtención de datos
        Usuario usuario = (Usuario) authentication.getPrincipal();
        Topico topico= topicoRepository.getReferenceById(id);
        topico.actualizarDatos(datos);
        topico.setAutor(usuario.getNombre());
        return  new DatosActualizarTopico(topico.getTitulo(), topico.getMensaje());
    }

    public void eliminarTopico(Long id) {
        interfaceValidPorIDS.forEach(v -> v.validar(id));
        /*
        1- obtenemos el tópico
        2- obtenemos el curso que contiene el tópico a eliminar
        3- eliminamos el tópico
        4- si el Curso ya no tiene tópicos tambien lo eliminamos
        */
        Topico topico = topicoRepository.getReferenceById(id);
        Curso curso= topico.getCurso();
        curso.getTopicos().remove(topico);
        if (curso.getTopicos().size()<1){
            cursoRepository.delete(curso);
        }
    }

    public Page<DatosTopico> listaTopico(Pageable paginacion) {
        return topicoRepository.findAll(paginacion).map(DatosTopico::new);
    }
}