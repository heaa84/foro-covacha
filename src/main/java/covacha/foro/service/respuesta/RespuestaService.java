package covacha.foro.service.respuesta;

import covacha.foro.domain.respuesta.Respuesta;
import covacha.foro.domain.respuesta.RespuestaRepository;
import covacha.foro.domain.respuesta.dto.DatosRegistrarRespuesta;
import covacha.foro.domain.respuesta.dto.DatosRespuesta;
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
    public Respuesta agregarRespuesta(@RequestBody @Valid DatosRegistrarRespuesta datos){
        // Validadores
        validadorRespuestas.forEach(v-> v.validar(datos));
        // obtener usuario autentificado
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        String nombreUsuario= auth.getName();
        // Buscar el tópico al que pertenece la respuesta
        Topico topico=topicoRepository.findById(datos.idTopico())
                .orElseThrow(()-> new IllegalArgumentException(
                        "El tópico con ID" + datos.idTopico()+"No existe."
                ));
        // Crear la respuesta
        Respuesta respuesta= new Respuesta();
        respuesta.setMensaje(datos.mensaje());
        respuesta.setUsuarioQueRespondio(nombreUsuario);
        respuesta.setTopico(topico);
        // Cambiar status del tópico
        topico.setStatus(TopicoStatus.RESUELTO);
        return  respuestaRepository.save(respuesta);
    };
}
