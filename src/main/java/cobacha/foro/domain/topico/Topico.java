package cobacha.foro.domain.topico;

import cobacha.foro.domain.curso.Curso;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "topicos")
@Entity(name = "Topico")
@Getter
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
    @NotBlank
    private String autor;

    private LocalDateTime fechaCreacion = LocalDateTime.now(); // Se asigna la fecha actual por defecto

    @NotNull
    @Enumerated(EnumType.STRING)
    private TopicoStatus status;

    // Relación muchos a uno: un tópico pertenece a un curso
    @OneToMany(mappedBy = "topico", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List <Curso> curso;

    // Constructor para inicializar usando un objeto de tipo DatosRegistroTopico
    public Topico(@Valid DatosRegistroTopicoConCurso datosRegistroTopicoConCurso) {
        this.titulo = datosRegistroTopicoConCurso.titulo();
        this.mensaje = datosRegistroTopicoConCurso.mensaje();
        this.autor=datosRegistroTopicoConCurso.autor();
        this.status= TopicoStatus.ACTIVO;
        Curso curso=new Curso();
        curso.setNombre(datosRegistroTopicoConCurso.nombreCurso());
        curso.setCategoria(datosRegistroTopicoConCurso.categoriaCurso());
        curso.setTopico(this);
        this.curso=List.of(curso);
    }
    public void actualizarDatos(DatosActualizarTopico datosActualizarTopico){
        if (datosActualizarTopico.titulo() != null) {
            this.titulo = datosActualizarTopico.titulo();
        }
        if (datosActualizarTopico.mensaje() != null) {
            this.mensaje = datosActualizarTopico.mensaje();
        }

    }
}
