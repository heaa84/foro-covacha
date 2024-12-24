package covacha.foro.service.usuario;

import covacha.foro.domain.usuario.Usuario;
import covacha.foro.domain.usuario.UsuarioRepository;
import covacha.foro.domain.usuario.dto.DatosUsuario;
import covacha.foro.service.usuario.validadores.InterfaceValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    List<InterfaceValid> interfaceValids;

    @Autowired
    UsuarioRepository usuarioRepository;

    public ResponseEntity <?> actualizarUsuario(long id, DatosUsuario datos) {
        interfaceValids.forEach(v->v.validar(datos));
        Usuario usuario=usuarioRepository.getReferenceById(id);
        usuario.actualizarDatos(datos);
        return ResponseEntity.ok(datos);
    }
    public DatosUsuario crearUsuario(DatosUsuario datos){
        interfaceValids.forEach(v->v.validar(datos));
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        //Encriptar contraseña
        BCryptPasswordEncoder encoder= new BCryptPasswordEncoder(10);
        String contrasenaBcryp= encoder.encode(datos.contrasena());
        Usuario usuarioNuevo= usuarioRepository.save(new Usuario(datos,contrasenaBcryp));
        return new DatosUsuario(usuarioNuevo);
    }

    public Page<DatosUsuario> listaUsuarios(Pageable paginacion) {
        return usuarioRepository.findAll(paginacion).map(DatosUsuario::new);
    }
}

