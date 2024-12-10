package cobacha.foro.domain.usuario;

import cobacha.foro.domain.usuarios.DatosActualizarUsuario;
import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository  extends JpaRepository<Usuario , Long> {
    UserDetails findByNombre(String nombre);

     boolean existsByNombre(String nombre);
}
