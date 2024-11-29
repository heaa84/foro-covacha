package cobacha.foro.domain.curso;

import cobacha.foro.domain.topico.Topico;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Table(name = "cursos")
@Entity(name = "Curso")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String categoria;

    @ManyToOne
    @JoinColumn(name = "topico_id") // Asegúrate de que este nombre de columna coincida con tu esquema de base de datos
    private Topico topico;

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setTopico(Topico topico) {
        this.topico = topico;
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