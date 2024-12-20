package covacha.foro.service.respuesta;

import covacha.foro.domain.respuesta.Respuesta;
import covacha.foro.domain.respuesta.RespuestaRepository;
import covacha.foro.domain.respuesta.dto.DatosRegistrarRespuesta;
import covacha.foro.domain.topico.Topico;
import covacha.foro.domain.topico.TopicoRepository;
import covacha.foro.domain.topico.TopicoStatus;
import covacha.foro.service.respuesta.validadores.InterfaceValid;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.util.List;

@Service
public class RespuestaService {
    @Autowired
    private List <InterfaceValid> validadorRespuestas;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private RespuestaRepository respuestaRepository;


    //metodos de solicitudes de clientes
    public ResponseEntity<?> agregarRespuesta(@RequestBody @Valid DatosRegistrarRespuesta datos){
        // Validadores
        validadorRespuestas.forEach(v-> v.validar(datos));

        // 1. Obtener usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nombreUsuario = auth.getName();
        System.out.println("Usuario autenticado: " + nombreUsuario);

        // 2. Buscar el tópico al que pertenece la respuesta
        var optionalTopico = topicoRepository.findById(datos.idTopico());
        if (optionalTopico.isEmpty()){
            return ResponseEntity.badRequest().body("El tópito con ID "+ datos.idTopico() + " no existe.");
        }

        Topico topico =optionalTopico.get();

        // 3. Crear la respuesta
        Respuesta respuesta = new Respuesta();
        respuesta.setMensaje(datos.mensaje());
        respuesta.setUsuarioQueRespondio(nombreUsuario);
        respuesta.setTopico(topico);

        // 4. Pasar estado de tópico a resuelto
        topico.setStatus(TopicoStatus.valueOf("RESUELTO"));

        // 5. Guardamos la respuesta en BD
        respuestaRepository.save(respuesta);

        // 6. retornar respuesta
        URI url = URI.create("/respuesta/" + respuesta.getId());
        return  ResponseEntity.created(url).body("Respuesta creada exitosamente");
    };
}
