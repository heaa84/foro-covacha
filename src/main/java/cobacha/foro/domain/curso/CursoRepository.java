package cobacha.foro.domain.curso;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    Optional<Curso> findByNombre(@NotBlank String nombre);
    // Puedes agregar métodos personalizados aquí si es necesario

}