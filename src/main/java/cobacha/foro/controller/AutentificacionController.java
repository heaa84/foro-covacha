package cobacha.foro.controller;

import cobacha.foro.domain.usuario.DatosAtuentificacionUsuario;
import cobacha.foro.domain.usuario.Usuario;
import cobacha.foro.infra.security.DatosJWTToken;
import cobacha.foro.infra.security.TokenService;
import jakarta.validation.Valid;
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

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity autentificacionUsuario(@RequestBody @Valid DatosAtuentificacionUsuario datosAtuentificacionUsuario){
        Authentication AuthToken =new UsernamePasswordAuthenticationToken(datosAtuentificacionUsuario.nombre(),datosAtuentificacionUsuario.contrasena());
        var usuarioAutenticado=authenticationManager.authenticate(AuthToken);
        var JWTToken=tokenService.generarToken((Usuario) usuarioAutenticado.getPrincipal());
        return ResponseEntity.ok(new DatosJWTToken(JWTToken));
    }
}
