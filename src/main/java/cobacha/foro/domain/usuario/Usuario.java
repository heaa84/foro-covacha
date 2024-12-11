package cobacha.foro.domain.usuario;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;

@Table(name = "usuarios")
@Entity(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
// La interfaceUserDetails nos ayudará, por medio del retorno de sus metodos a indicarle, entre otras cosas, que parametros seran nuestros usuarios y contraseñasa,
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String correo_electronico;
    private String contrasena;
    private String perfil;

    public Usuario(@Valid DatosRegistrarNuevoUsuario datosRegistrarNuevoUsuario, String contrasenaBcryp) {
    this.nombre = datosRegistrarNuevoUsuario.nombre();
    this.correo_electronico = datosRegistrarNuevoUsuario.correo_electronico();


    this.contrasena = contrasenaBcryp;
    this.perfil = datosRegistrarNuevoUsuario.perfil();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Validación de perfil
        if (!this.perfil.equalsIgnoreCase("ADMIN") && !this.perfil.equalsIgnoreCase("USER")) {
            throw new IllegalArgumentException("Perfil no válido: " + this.perfil);
        }
        // Asignación de la autoridad basada en el perfil
        String rol = "ROLE_" + this.perfil.toUpperCase();
        return List.of(new SimpleGrantedAuthority(rol));
    }


    @Override
    public String getPassword() {
        return this.contrasena;
    }

    @Override
    public String getUsername() {
        return this.nombre;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void actualizarDatos(DatosActualizarUsuario datosActualizarUsuario) {
    if (datosActualizarUsuario.nombre()!=null){
        this.nombre= datosActualizarUsuario.nombre();
    }

    if (datosActualizarUsuario.correo_electronico()!=null){
        this.correo_electronico= datosActualizarUsuario.correo_electronico();
    }
        if (datosActualizarUsuario.contrasena()!=null){
            String contrasenaNueva = datosActualizarUsuario.contrasena();
            BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(10);
            String result=encoder.encode(contrasenaNueva);
            System.out.println(result);
            this.contrasena= result;
        }
        if(datosActualizarUsuario.perfil()!=null){
            if (datosActualizarUsuario.perfil().equals("ADMIN") || datosActualizarUsuario.perfil().equals("USER")){
                this.perfil= datosActualizarUsuario.perfil();
            }
        }
    }
}
