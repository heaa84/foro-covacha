# Creando Spring Security key con Token JWT (JSON Web Tokens).
## Estructura:
- covacha.foro/controller/
  - AutenticationController.java
    - Contiene el endpoint "/login". 
    - Capturamos en un Authentication el usuario y contraseña del usuario.
    - Método que autentifica el token.
    - Método que genera el token.
    - retorna el token en formato JSON.
- covacha.foro/infra/security
  - TokenService
    - Método que genera el JWT si los datos del usuario son válidos.
    - Método que verifica que el JWT sea válido.
    - Método que genera la fecha y hora, de expiración del token.
  - SecurityConfigurations.java
    - Configuración de la cadena de filtros de seguridad.
    - Configuración del AuthenticationManager.
    - Configuración del codificador de contraseñas.
  - SecurityFilter.java
    - Método que se ejecuta con cada solicitud HTTP. este método intenta autenticar al usuario mediante el token incluido en la cabecera de la solicitud.
    - Método para extraer el token de autenticación de la cabecera "Authorization" de la solicitud.
  - AutenticacionService.java
    - Contiene un método que; busca por nombre un usuario y lo retorna como un UserDetails. 
  - DatosJWTToken.java
    - Contiene un DTO y se ocupa para retornar el JWT.
  
# Diagrama;
## Diagrama de flujo para la autenticación con JWT en Foro Cobacha
![Diagrama_spring_boot_segurity.png](src%2Fmain%2Fresources%2Fimg%2FDiagrama_spring_boot_segurity.png)

## codigo:
```java
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
```
```java
package covacha.foro.infra.security;

import covacha.foro.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacionService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository; // Inyectar repisiyorio de usuarios 

    /* Servicio para decirle a Spring que queremos personalizar el flujo de autentificación que tenemos por defecto
    Devolvemos un UserDetails que lo carga por nombre de usuario
    */
    @Override
    public UserDetails loadUserByUsername(String nombre) throws UsernameNotFoundException {
        return usuarioRepository.findByNombre(nombre);
    }
}

```
```java
package covacha.foro.infra.security;
// DTO PARA QUE DEVULVA A CLIENTE EL TOKEN JWT EN FORMATO JSON
public record DatosJWTToken(String jwtToken) {
}

```
```java
package covacha.foro.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    // Configuración de la cadena de filtros de seguridad
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Configurar sesiones sin estado
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,"/login").permitAll() // Rutas públicas sin autenticación
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()) // Todas las demás rutas requieren autenticación
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    // Configuración del AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Configuración del codificador de contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

```
```java
package covacha.foro.infra.security;

import covacha.foro.domain.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de seguridad que se ejecuta una vez por cada solicitud HTTP.
 * Este filtro se encarga de autenticar al usuario utilizando un token de autenticación.
 */
@Component
public class SecurityFilter extends OncePerRequestFilter {

    /**
     * Servicio que se encarga de manejar la lógica relacionada con los tokens.
     */
    @Autowired
    private TokenService tokenService;

    /**
     * Repositorio para acceder a la información de los usuarios almacenados en la base de datos.
     */
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Método que se ejecuta con cada solicitud HTTP.
     * Este método intenta autenticar al usuario mediante el token incluido en la cabecera de la solicitud.
     *
     * @param request la solicitud HTTP entrante
     * @param response la respuesta HTTP que se enviará
     * @param filterChain la cadena de filtros de seguridad
     * @throws ServletException si ocurre un error en la lógica de filtrado
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Obtener el token de la cabecera "Authorization" de la solicitud
        var authHeader = recuperarToken(request);

        // Verificar si el token no es nulo
        if (authHeader != null) {
            // Obtener el "subject" (nombre de usuario) a partir del token
            var subject = tokenService.getSubject(authHeader);

            // Buscar al usuario en la base de datos por su nombre de usuario
            var usuario = usuarioRepository.findByNombre(subject);

            // Crear un objeto de autenticación y establecerlo en el contexto de seguridad
            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }

    /**
     * Método para extraer el token de autenticación de la cabecera "Authorization" de la solicitud.
     *
     * @param request la solicitud HTTP entrante
     * @return el token de autenticación (sin el prefijo "Bearer "), o null si no está presente
     */
    private String recuperarToken(HttpServletRequest request) {
        // Obtener la cabecera "Authorization" de la solicitud
        var authorization = request.getHeader("Authorization");

        // Verificar si la cabecera contiene un valor
        if (authorization != null) {
            // Remover el prefijo "Bearer " del token y devolverlo
            return authorization.replace("Bearer ", "");
        }

        // Devolver null si no hay token en la cabecera
        return null;
    }
}

```
```java
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
        return LocalDateTime.now().plusHours(200).toInstant(ZoneOffset.of("-06:00")); // para producion cambiar a 2 horas
    }
}
````


