package cobacha.foro.domain.usuarios;

import cobacha.foro.domain.usuario.Usuario;
import jakarta.validation.constraints.NotBlank;

public record DatosRegistrarNuevoUsuario(
        @NotBlank
        String nombre,
        @NotBlank
                String correo_electronico,
        @NotBlank
        String contrasena,
        @NotBlank
        String perfil
) {
    public DatosRegistrarNuevoUsuario (Usuario usuario){
        this(usuario.getNombre(), usuario.getCorreo_electronico(), usuario.getContrasena(), usuario.getPerfil());
    }
}
