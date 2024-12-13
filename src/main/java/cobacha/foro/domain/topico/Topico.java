package cobacha.foro.domain.topico;

import cobacha.foro.domain.curso.Curso;
import cobacha.foro.domain.topico.dto.DatosActualizarTopico;
import cobacha.foro.domain.topico.dto.DatosRegistroTopicoConCurso;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;


@Table(name = "topicos")
@Entity(name = "Topico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String titulo;
    @NotBlank
    private String mensaje;
    private String autor;

    private LocalDateTime fechaCreacion = LocalDateTime.now(); // Se asigna la fecha actual por defecto

    @NotNull
    @Enumerated(EnumType.STRING)
    private TopicoStatus status;

    // Relación muchos a uno: un tópico pertenece a un curso

    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false) // Asegúrate de que este nombre de columna coincida con tu esquema de base de datos
    private Curso curso;

    // Constructor para inicializar usando un objeto de tipo DatosRegistroTopico
    public Topico(@Valid DatosRegistroTopicoConCurso datosRegistroTopicoConCurso) {
        this.titulo = datosRegistroTopicoConCurso.titulo();
        this.mensaje = datosRegistroTopicoConCurso.mensaje();
        this.status= TopicoStatus.ACTIVO;
    }




    public void actualizarDatos(DatosActualizarTopico datosActualizarTopico){
        if (datosActualizarTopico.titulo() != null) {
            this.titulo = datosActualizarTopico.titulo();
        }
        if (datosActualizarTopico.mensaje() != null) {
            this.mensaje = datosActualizarTopico.mensaje();
        }
        if (datosActualizarTopico.autor() != null) {
            this.autor = datosActualizarTopico.autor();
        }

    }
}

