package covacha.foro.controller;

import covacha.foro.domain.usuario.dto.DatosAtuentificacionUsuario;
import covacha.foro.domain.usuario.Usuario;
import covacha.foro.infra.security.DatosJWTToken;
import covacha.foro.infra.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(
            summary = "Autentificar usuario",
            description = "Autentificar usuario, por default nombre: admin, contraseña: admin")
    public ResponseEntity autentificacionUsuario(@RequestBody @Valid DatosAtuentificacionUsuario datosAtuentificacionUsuario){
        Authentication AuthToken =new UsernamePasswordAuthenticationToken(datosAtuentificacionUsuario.nombre(),datosAtuentificacionUsuario.contrasena());
        var usuarioAutenticado=authenticationManager.authenticate(AuthToken);
        var JWTToken=tokenService.generarToken((Usuario) usuarioAutenticado.getPrincipal());
        return ResponseEntity.ok(new DatosJWTToken(JWTToken));
    }
}

