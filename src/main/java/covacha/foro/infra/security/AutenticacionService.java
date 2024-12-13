package covacha.foro.infra.security;

import covacha.foro.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacionService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository; // Inyectar repisiyorio de usuarios 

    /* Servicio para decirle a Spring que queremos personalizar el flujo de autentificacion que tenemos por defecto
    Devolvemos un UserDetails que lo carga por nombré de usuario
    */
    @Override
    public UserDetails loadUserByUsername(String nombre) throws UsernameNotFoundException {
        return usuarioRepository.findByNombre(nombre);
    }
}
