package covacha.foro.service.usuario;

import covacha.foro.domain.usuario.Usuario;
import covacha.foro.domain.usuario.UsuarioRepository;
import covacha.foro.domain.usuario.dto.DatosUsuario;
import covacha.foro.service.usuario.validadores.InterfaceValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

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
    public ResponseEntity <?> crearUsuario(DatosUsuario datosUsuario){
        interfaceValids.forEach(v->v.validar(datosUsuario));
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        //Encriptar contraseña
        BCryptPasswordEncoder encoder= new BCryptPasswordEncoder(10);
        String contrasenaBcryp= encoder.encode(datosUsuario.contrasena());
        usuarioRepository.save(new Usuario(datosUsuario,contrasenaBcryp));
        return ResponseEntity.ok(datosUsuario);
    }
}

