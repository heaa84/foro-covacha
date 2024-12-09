package cobacha.foro.domain.usuario;

public record DatosListadoUsuario(
        Long id,
        String nombre,
        String correo_electronico,
        String perfil
) {
    // Constructor
    public DatosListadoUsuario(Usuario usuario){
        this(usuario.getId(),usuario.getNombre(), usuario.getCorreo_electronico(), usuario.getPerfil());
    }
}
