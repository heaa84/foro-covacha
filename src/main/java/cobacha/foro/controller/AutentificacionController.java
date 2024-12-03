package cobacha.foro.controller;

import cobacha.foro.domain.usuarios.DatosAtuentificacionUsuario;
import jakarta.validation.Valid;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutentificacionController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity autentificacionUsuario(@RequestBody @Valid DatosAtuentificacionUsuario datosAtuentificacionUsuario){
        Authentication token =new UsernamePasswordAuthenticationToken(datosAtuentificacionUsuario.nombre(),datosAtuentificacionUsuario.contrasena());
        authenticationManager.authenticate(token);
        return ResponseEntity.ok().build();
    }
}
