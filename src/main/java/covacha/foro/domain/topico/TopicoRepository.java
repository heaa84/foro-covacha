package covacha.foro.domain.topico;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, Long> {
     // Verifica si ya existe un tópico con el mismo título y mensaje

     boolean existsByTituloAndMensaje(String titulo, String mensaje);

    List<Topico> findAllById(Long id);
    // Puedes agregar métodos personalizados si es necesario, por ejemplo


}
