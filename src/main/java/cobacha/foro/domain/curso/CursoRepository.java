package cobacha.foro.domain.curso;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    Curso findByNombre(@NotBlank String nombre);

    boolean existsByNombreAndCategoria(@NotBlank String nombre, @NotBlank String categoria);

    Optional<Curso> findByNombreAndCategoria(@NotBlank String nombre, @NotBlank String categoria);
    // Puedes agregar métodos personalizados aquí si es necesario

}