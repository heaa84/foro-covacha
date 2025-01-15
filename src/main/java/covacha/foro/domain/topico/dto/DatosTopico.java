package covacha.foro.domain.topico.dto;

import covacha.foro.domain.respuesta.dto.DatosRespuesta;
import covacha.foro.domain.topico.Topico;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public record DatosTopico(
        Long id,
        String autor,
        String titulo,
        String mensaje,
        String fechaCreacion,
        String nombre,
        String categoria,
        List<Object> respuestas
) {
    // Constructor
    public DatosTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getAutor(),
                topico.getTitulo(),
                topico.getMensaje(),
                formatearFecha(topico.getFechaCreacion()),
                topico.getCurso().getNombre(),
                topico.getCurso().getCategoria(),
                verificarRespuestas(topico)
        );
    }

    // Método para formatear la fecha
    private static String formatearFecha(LocalDateTime fecha) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return fecha.format(formatter);
    }

    // Método para verificar si hay respuestas
    private static List<Object> verificarRespuestas(Topico topico) {
        if (topico.getRespuestas() == null || topico.getRespuestas().isEmpty()) {
            // Crear una lista con un mensaje indicando que no hay respuestas
            return List.of("No hay respuestas disponibles para este tópico");
        }
        // Si hay respuestas, procesarlas normalmente
        return topico.getRespuestas().stream().map(DatosRespuesta::new).collect(Collectors.toList());
    }
}
