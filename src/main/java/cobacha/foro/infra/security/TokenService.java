package cobacha.foro.infra.security;

import cobacha.foro.domain.usuarios.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
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
                    .withClaim("id",usuario.getId()) // ID de l usuario
                    .withExpiresAt(generarFechaExpiracio()) // Tiempo de expidacion del JWT "tenemos que crear un metodo instan"
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException();
            // Invalid Signing configuration / Couldn't convert Claims.
        }
    }
    // Validando token /JWTVerifier
    public String getSubject(String token) {
        DecodedJWT verifier = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret); // algorito tiene que ser el mismo que ocupamos para la generacion del token
            verifier = JWT.require(algorithm)
                    .withIssuer("cobacha")
                    .build()
                    .verify(token);
            verifier.getSubject();
        } catch (JWTVerificationException exception) {
            // Invalid signature/claims
            System.out.println(exception.toString()
            );
        }
        if (verifier.getSubject()==null) {
            throw new RuntimeException("Verifiel invalido");
        }
        return verifier.getSubject();
    }

    // GENERAMOS LA FECHA DE EXPIRACIO O TIEMPO EN QUE ES VALIDO EL TOKEN
    public Instant generarFechaExpiracio(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-06:00"));
    }
}
