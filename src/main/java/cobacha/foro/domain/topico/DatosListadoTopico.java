package cobacha.foro.domain.topico;

import java.time.LocalDateTime;

public record DatosListadoTopico(
        Long id,
        String autor,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        String nombre,
        String categoria

) {
    // Constructor
    public DatosListadoTopico(Topico topico){
        this(topico.getId(),topico.getAutor(),topico.getTitulo(), topico.getMensaje(), topico.getFechaCreacion(), topico.getCurso().getNombre(), topico.getCurso().getCategoria());
    }
}
