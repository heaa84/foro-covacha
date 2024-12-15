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
        // crea variable authenticationToken a partir de una clase UsernamePasswordAuthenticationToken(datosAtuentificacionUsuario.nombre(),datosAtuentificacionUsuario.contrasena())
        // Esto nos crea un nuevo objeto de tipo Authentication
        Authentication authenticationToken =new UsernamePasswordAuthenticationToken(datosAtuentificacionUsuario.nombre(),datosAtuentificacionUsuario.contrasena());
        // Usamos usuarioAutenticado=authenticationManager.authenticate(authenticationToken) donde, mandamos authenticationToken como parámetro si el método consigue autenticar los datos,
        // Devuelve un objeto con los datos de la autentificación,
        // Y lo guardamos en usuarioAutentificado
        var usuarioAutentificado=authenticationManager.authenticate(authenticationToken);
        // El método tokenService.generarToken nos ayuda a generar el JWT, tenemos que pasarle el usuario principal ya autentificado.
        // lo guardamos en JWTToken
        var JWTToken=tokenService.generarToken((Usuario) usuarioAutentificado.getPrincipal());
        // Retornamos el token en formato JSON
        return ResponseEntity.ok(new DatosJWTToken(JWTToken));
    }
}

