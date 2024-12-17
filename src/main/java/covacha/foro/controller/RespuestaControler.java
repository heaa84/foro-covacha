package covacha.foro.controller;


import covacha.foro.domain.respuesta.Respuesta;
import covacha.foro.domain.respuesta.RespuestaRepository;
import covacha.foro.domain.respuesta.dto.DatosRegistrarRespuesta;
import covacha.foro.domain.topico.Topico;
import covacha.foro.domain.topico.TopicoRepository;
import covacha.foro.domain.topico.TopicoStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;


@RestController
@RequestMapping("/respuesta")
@SecurityRequirement(name = "bearer-key")
public class RespuestaControler {
    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private RespuestaRepository respuestaRepository;

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping
    @Transactional
    @Operation(
            summary = "Agregar respuesta",
            description = "Agregar una respuesta al topico"
    )
    public ResponseEntity<?> agregarResouesta(@RequestBody @Valid DatosRegistrarRespuesta datosRegistrarRespuesta){
        // 1. Obtener usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nombreUsuario = auth.getName();
        System.out.println("Usuario autenticado: " + nombreUsuario);

        // 2. Buscar el tópico al que pertenece la respuesta
        var optionalTopico = topicoRepository.findById(datosRegistrarRespuesta.idTopico());
        if (optionalTopico.isEmpty()){
            return ResponseEntity.badRequest().body("El tópito con ID "+ datosRegistrarRespuesta.idTopico() + " no existe.");
        }

        Topico topico =optionalTopico.get();

        // 3. Crear la respuesta
        Respuesta respuesta = new Respuesta();
        respuesta.setMensaje(datosRegistrarRespuesta.mensaje());
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
