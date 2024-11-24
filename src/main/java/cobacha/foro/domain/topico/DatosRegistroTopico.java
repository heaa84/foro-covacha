package cobacha.foro.domain.topico;


import jakarta.validation.constraints.NotBlank;


public record DatosRegistroTopico(
        @NotBlank(message = "El título del tópico no puede estar vacío") String titulo,
        @NotBlank(message = "El mensaje del tópico no puede estar vacío") String mensaje,
        @NotBlank(message = "El autor del topico no puede estar vacío") String autor
)
        //@NotNull(message = "El estado del tópico es obligatorio") TopicoStatus status)
        {
}
