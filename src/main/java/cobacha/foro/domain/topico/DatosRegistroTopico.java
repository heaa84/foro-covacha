package cobacha.foro.domain.topico;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosRegistroTopico(
        @NotBlank(message = "El título del tópico no puede estar vacío") String titulo,
        @NotBlank(message = "El mensaje del tópico no puede estar vacío") String mensaje)
        //@NotNull(message = "El estado del tópico es obligatorio") TopicoStatus status)
        {
}
