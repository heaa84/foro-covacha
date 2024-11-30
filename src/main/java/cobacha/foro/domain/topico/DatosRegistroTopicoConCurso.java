package cobacha.foro.domain.topico;

import jakarta.validation.constraints.NotBlank;

public record DatosRegistroTopicoConCurso(

            @NotBlank(message = "El título del tópico no puede estar vacío") String titulo,
            @NotBlank(message = "El mensaje del tópico no puede estar vacío") String mensaje,
            @NotBlank(message = "El autor del topico no puede estar vacío") String autor,
            @NotBlank String nombre, // nombre del curso / Operaciones Basicas java
            @NotBlank String categoria // categoria del curso / Java
){
}