package cobacha.foro.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
/* Filtro de token*/
@Component
public class SecurityFilter extends OncePerRequestFilter {
@Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Odtener el Token del header
        var token= request.getHeader("Authorization");
        if (token !=null ){
            token=token.replace("Bearer ", "");
        }
        filterChain.doFilter(request,response); // Mandar Request y Responce
    }
}
