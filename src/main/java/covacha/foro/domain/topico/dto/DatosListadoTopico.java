package covacha.foro.domain.topico.dto;

import covacha.foro.domain.respuesta.dto.DatosRespuesta;
import covacha.foro.domain.topico.Topico;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record DatosListadoTopico(
        Long id,
        String autor,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        String nombre,
        String categoria,
        List<DatosRespuesta> respuestas
) {
    // Constructor
    public DatosListadoTopico(Topico topico){
        this(
                topico.getId(),
                topico.getAutor(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getCurso().getNombre(),
                topico.getCurso().getCategoria(),
                topico.getRespuestas().stream().map(DatosRespuesta::new).collect(Collectors.toList()));
    }
}
