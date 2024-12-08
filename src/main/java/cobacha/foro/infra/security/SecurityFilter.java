package cobacha.foro.infra.security;

import cobacha.foro.domain.topico.Topico;
import cobacha.foro.domain.usuarios.Usuario;
import cobacha.foro.domain.usuarios.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
/* Filtro de token*/
@Component
public class SecurityFilter extends OncePerRequestFilter {
@Autowired
private TokenService tokenService;
@Autowired
private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Odtener el Token del header
        var authHeader = recuperarToken(request);
        if (authHeader !=null ){
            var subject = tokenService.getSubject(authHeader);
            var usuario = usuarioRepository.findByNombre(subject);

            var authentication=new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        filterChain.doFilter(request,response); // Mandar Request y Responce
    }


    private String recuperarToken(HttpServletRequest request) {
        var authorization = request.getHeader("Authorization");
        if (authorization!=null) {
            return authorization.replace("Bearer ","");
        }
        return null;
    }
}
