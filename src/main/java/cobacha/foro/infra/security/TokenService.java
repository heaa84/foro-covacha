package cobacha.foro.infra.security;

import cobacha.foro.domain.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
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
    private DecodedJWT decodedJWT;

    // GENERAMOS RL TOKEN
    public  String generarToken(Usuario usuario){
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.create()
                    .withIssuer("cobacha") // Quien emite el token
                    .withSubject(usuario.getNombre()) // Usuario que emite el token
                    .withClaim("id",usuario.getId()) // ID de el usuario
                    .withExpiresAt(generarFechaExpiracio()) // Tiempo de expidacion del JWT "tenemos que crear un metodo instan"
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Error al generar token JWT", exception);
        }
    }
    // Validando token /JWTVerifier
    public String getSubject(String token) throws RuntimeException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret); // algorito tiene que ser el mismo que ocupamos para la generacion del token
            String subject= JWT.require(algorithm)
                    .withIssuer("cobacha")
                    .build()
                    .verify(token)
                    .getSubject();
            System.out.println(subject);
            return subject;
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token no valido o expirado!!");
        }
    }

    // GENERAMOS LA FECHA DE EXPIRACIO O TIEMPO EN QUE ES VALIDO EL TOKEN
    public Instant generarFechaExpiracio(){
        return LocalDateTime.now().plusHours(200).toInstant(ZoneOffset.of("-06:00")); // para producion cambiar a 2 horas
    }
}
