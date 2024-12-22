package covacha.foro.domain.curso;


import covacha.foro.domain.curso.dto.DatosActualizarCurso;
import covacha.foro.domain.topico.dto.DatosRegistroTopicoConCurso;
import covacha.foro.domain.topico.Topico;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

import java.util.List;


@Table(name = "cursos")
@Entity(name = "Curso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String categoria;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL , orphanRemoval = true, fetch = FetchType.EAGER)
    private List <Topico> topicos;

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setTopicos(List<Topico> topicos) {
        this.topicos = topicos;
    }

    public Curso(@Valid DatosRegistroTopicoConCurso datosRegistroTopicoConCurso) {
        this.nombre = datosRegistroTopicoConCurso.nombre();
        this.categoria = datosRegistroTopicoConCurso.categoria();
    }

    public void actualizarDatos(DatosActualizarCurso datos) {
        if (nombre != null) {
            this.nombre =datos.nombre();
        }
        if (categoria != null) {
            this.categoria = datos.categoria();
        }
    }

    public static record DatosListadoCursos(
            Long id,
            String nombre,
            String categoria

    ) {
        // Constructor

        public DatosListadoCursos(Curso curso) {
            this(curso.getId(), curso.getNombre(), curso.getCategoria());
        }
    }
}