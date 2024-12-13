package covacha.foro.domain.usuario.dto;

import covacha.foro.domain.usuario.Usuario;

public record DatosActualizarUsuario(
        String nombre,
        String correo_electronico,
        String contrasena,
        String perfil
) {
    // Constructor
    public DatosActualizarUsuario(Usuario usuario){
        this(usuario.getNombre(), usuario.getCorreo_electronico(), usuario.getContrasena(), usuario.getPerfil());
    }
}
