package covacha.foro.domain.respuesta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosRegistrarRespuesta(
        @NotNull(message = "El ID del tópico no puede ser nulo")
        Long idTopico,

        @NotBlank(message = "El mensaje de la respuesta no puede estar vacío")
        String mensaje
) { }
