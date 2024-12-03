package cobacha.foro.infra.security;

import cobacha.foro.domain.usuarios.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
// SERVIO PARA GENERAR TOQUEN
@Service
public class TokenService {
    @Value("${api.security.secret}") // VARIABLE DE ENTORNO QUE ESTA EN PROPETIS
    private String apiSecret; //VARIABLE CON EL VALOR DE LA VARIANLE DE ENTORNO
    // GENERAMOS RL TOKEN
    public  String generarToken(Usuario usuario){
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.create()
                    .withIssuer("cobacha") // Quien emite el token
                    .withSubject(usuario.getNombre()) // Usuario que emite el token
                    .withClaim("id",usuario.getId()) // ID de l usuario
                    .withExpiresAt(generarFechaExpiracio()) // Tiempo de expidacion del JWT "tenemos que crear un metodo instan"
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException();
            // Invalid Signing configuration / Couldn't convert Claims.
        }
    }
    // GENERAMOS LA FECHA DE EXPIRACIO O TIEMPO EN QUE ES VALIDO EL TOKEN
    public Instant generarFechaExpiracio(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-06:00"));
    }
}
