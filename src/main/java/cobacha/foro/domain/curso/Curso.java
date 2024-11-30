package cobacha.foro.domain.curso;


import cobacha.foro.domain.topico.DatosRegistroTopicoConCurso;
import cobacha.foro.domain.topico.Topico;
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

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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

    public void actualizarDatos(String nombre, String categoria) {
        if (nombre != null) {
            this.nombre = nombre;
        }
        if (categoria != null) {
            this.categoria = categoria;
        }
    }
}