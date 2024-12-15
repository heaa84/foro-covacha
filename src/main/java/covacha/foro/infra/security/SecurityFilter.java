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
