package cobacha.foro.domain.topico.dto;

public record DatosActualizarTopico(
        String titulo,
        String mensaje,
        String autor,
        String nombre,
        String categoria
) {
}
