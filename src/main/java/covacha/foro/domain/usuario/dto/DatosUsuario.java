package covacha.foro.domain.usuario.dto;

import covacha.foro.domain.usuario.Usuario;
import jakarta.validation.constraints.NotBlank;

public record DatosUsuario(
        @NotBlank
        String nombre,
        @NotBlank
        String correo_electronico,
        @NotBlank
        String contrasena,
        @NotBlank
        String perfil
) {
    // Constructor
    public DatosUsuario(Usuario usuario){
        this(usuario.getNombre(), usuario.getCorreo_electronico(), usuario.getContrasena(), usuario.getPerfil());
    }
}
