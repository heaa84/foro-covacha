package covacha.foro.controller;


import covacha.foro.domain.respuesta.Respuesta;
import covacha.foro.domain.respuesta.dto.DatosRegistrarRespuesta;
import covacha.foro.service.respuesta.RespuestaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;


@RestController
@RequestMapping("/respuesta")
@SecurityRequirement(name = "bearer-key")
public class RespuestaController {
    @Autowired
    private RespuestaService respuestaService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping
    @Transactional
    @Operation(
            summary = "Agregar respuesta",
            description = "Agregar una respuesta al topico"
    )
    public ResponseEntity<?> agregarRespuesta(@RequestBody @Valid DatosRegistrarRespuesta datos) {
        Respuesta respuesta = respuestaService.agregarRespuesta(datos);
        URI url = URI.create("/respuesta/" + respuesta.getId());
        return ResponseEntity.created(url).body(respuesta);
    }
}
