package covacha.foro.domain.topico;

import covacha.foro.domain.curso.Curso;
import covacha.foro.domain.respuesta.Respuesta;
import covacha.foro.domain.topico.dto.DatosActualizarTopico;
import covacha.foro.domain.topico.dto.DatosRegistroTopicoConCurso;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


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
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion; // Se asigna la fecha actual por defecto
    @NotNull
    @Enumerated(EnumType.STRING)
    private TopicoStatus status;

    // Relación muchos a uno: un tópico pertenece a un curso

    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false) // Asegúrate de que este nombre de columna coincida con tu esquema de base de datos
    private Curso curso;
    // Relacion con respuesta
    @OneToMany(mappedBy = "topico", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Respuesta> respuestas = new ArrayList<>();

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
    }

    //Métodos para respuesta
    public void addRespuesta(Respuesta respuesta) {
        respuesta.setTopico(this);
        this.respuestas.add(respuesta);
    }

    public void removeRespuesta(Respuesta respuesta) {
        respuesta.setTopico(null);
        this.respuestas.remove(respuesta);
    }
}

