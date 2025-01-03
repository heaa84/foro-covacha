package covacha.foro.infra.security;

import covacha.foro.domain.usuario.Usuario;
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

    // Método que genera el JWT si los datos del usuario son válidos.
    public  String generarToken(Usuario usuario){
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.create()
                    .withIssuer("covacha") // Quien emite el token
                    .withSubject(usuario.getNombre()) // Usuario que emite el token
                    .withClaim("id",usuario.getId()) // ID de el usuario
                    .withExpiresAt(generarFechaExpiracio()) // Tiempo de expidacion del JWT "tenemos que crear un metodo instan"
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Error al generar token JWT", exception);
        }
    }
    // Método que verifica que el JWT sea válido.
    public String getSubject(String token) throws RuntimeException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret); // algorito tiene que ser el mismo que ocupamos para la generacion del token
            String subject= JWT.require(algorithm)
                    .withIssuer("covacha")
                    .build()
                    .verify(token)
                    .getSubject();
            System.out.println(subject);
            return subject;
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token no valido o expirado!!");
        }
    }

    // Método que genera la fecha y hora, de expiración del token
    public Instant generarFechaExpiracio(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-06:00")); // para producion cambiar a 2 horas
    }
}
