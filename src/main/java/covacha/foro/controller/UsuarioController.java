package covacha.foro.controller;
import covacha.foro.domain.usuario.dto.DatosListadoUsuario;
import covacha.foro.domain.usuario.Usuario;
import covacha.foro.domain.usuario.UsuarioRepository;
import covacha.foro.domain.usuario.dto.DatosUsuario;
import covacha.foro.domain.usuario.dto.DatosRegistrarNuevoUsuario;
import covacha.foro.infra.errores.TratadorDeErrores;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "bearer-key")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;


    // Listar todos los Usuarios
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping
    // Configuramos lo que va a mostrar swagger de este método
    @Operation(
            summary = "Obtiene la lista de usuarios",
            description = "Retorna todos los usuarios sin requerir parámetros de entrada")
    public ResponseEntity<Page<DatosListadoUsuario>> listadoUsuarios(
            @Parameter(hidden = true) // Ocultar parámetros para evitar que swagger los pida
            @PageableDefault(size = 10 , sort = "id") Pageable paginacion) {
        return ResponseEntity.ok(usuarioRepository.findAll(paginacion).map(DatosListadoUsuario::new));
    }

    // Actualizar los Usuarios
    // Solo actualizar Nombre, Email, Contraseña y perfil
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Transactional
    @Operation(
            summary = "Actualiza un usuario por ID",
            description = "Modifica los datos de un usuario, seleccionado por su id @PathVariable")
    public ResponseEntity<?> actualizarUsuario (@PathVariable Long id, @RequestBody @Valid DatosUsuario datosActualizarUsuario){
        return ResponseEntity.ok(actualizarUsuario(id,datosActualizarUsuario));
    }
    // crear usuarios nuevo solo ADMMIN
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Transactional
    @Operation(
            summary = "Registrar nuevo usuario",
            description = "Registra un usuario nuevo solo ADMIN")
    public ResponseEntity<?> crearUsuario(@RequestBody @Valid DatosUsuario datosUsuario){

        return ResponseEntity.ok(crearUsuario(datosUsuario));
    }
}
