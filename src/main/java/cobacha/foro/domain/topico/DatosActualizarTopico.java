package cobacha.foro.domain.topico;

public record DatosActualizarTopico(
        String titulo,
        String mensaje,
        String autor,
        String nombre,
        String categoria
) {
}
