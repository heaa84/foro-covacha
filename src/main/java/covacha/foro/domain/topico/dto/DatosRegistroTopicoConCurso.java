package covacha.foro.domain.topico.dto;

import jakarta.validation.constraints.NotBlank;

public record DatosRegistroTopicoConCurso(

            @NotBlank(message = "El título del tópico no puede estar vacío") String titulo,
            @NotBlank(message = "El mensaje del tópico no puede estar vacío") String mensaje,
            @NotBlank String nombre, // nombre del curso / Operaciones Basicas java
            @NotBlank String categoria // categoria del curso / Java
){

}