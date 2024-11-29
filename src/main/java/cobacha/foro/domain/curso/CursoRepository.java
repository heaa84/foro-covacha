package cobacha.foro.domain.curso;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    // Puedes agregar métodos personalizados aquí si es necesario
    // retorna curso por medio de la llave foranea
    Curso findByTopicoId(Long topicoId);
}