package cobacha.foro.controller;

import cobacha.foro.domain.usuario.DatosListadoUsuario;
import cobacha.foro.domain.usuario.Usuario;
import cobacha.foro.domain.usuario.UsuarioRepository;
import cobacha.foro.domain.usuarios.DatosActualizarUsuario;
import cobacha.foro.domain.usuarios.DatosRegistrarNuevoUsuario;
import cobacha.foro.infra.errores.TratadorDeErrores;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;


    // Listar todos los Usuarios
    @GetMapping
    public ResponseEntity<Page<DatosListadoUsuario>> listadoUsuarios(@PageableDefault(size = 10 , sort = "id") Pageable paginacion) {
        return ResponseEntity.ok(usuarioRepository.findAll(paginacion).map(DatosListadoUsuario::new));
    }

    // Actualizar los Usuarios
    // Solo actualizar Nombre, Email, Contraseña y perfil
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> actualizarUsuario (@PathVariable Long id, @RequestBody @Valid DatosActualizarUsuario datosActualizarUsuario){
        Optional <Usuario> usuariobd=usuarioRepository.findById(id); // Guardar el usuario de la BD en opctional si existe
        if (usuariobd.isPresent()){
            Usuario usuario=usuarioRepository.getReferenceById(usuariobd.get().getId());
            usuario.actualizarDatos(datosActualizarUsuario);
        }else {
            TratadorDeErrores errorResponse = new TratadorDeErrores("Usuario no existe");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        return ResponseEntity.ok(datosActualizarUsuario);
    }
    @PostMapping
    @Transactional
    public ResponseEntity<?> crearUsuario(@RequestBody @Valid DatosRegistrarNuevoUsuario datosRegistrarNuevoUsuario){

    }
}
