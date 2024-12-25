package covacha.foro.controller;

import covacha.foro.domain.usuario.dto.DatosUsuario;
import covacha.foro.service.usuario.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    private DatosUsuario datosUsuario;

    @BeforeEach
    void setUp() {
        datosUsuario = new DatosUsuario(
                "UsuarioValido",
                "usuario@valido.com",
                "contrasena123",
                "usuario"
        );
    }

    @Test
    void listadoUsuarios_retornaListaDeUsuarios() throws Exception {
        Page<DatosUsuario> paginaUsuarios = new PageImpl<>(List.of(datosUsuario));
        Mockito.when(usuarioService.listaUsuarios(any(Pageable.class))).thenReturn(paginaUsuarios);

        mockMvc.perform(get("/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nombre").value("UsuarioValido"))
                .andExpect(jsonPath("$.content[0].correo_electronico").value("usuario@valido.com"));
    }

    @Test
    void actualizarUsuario_actualizaUsuarioExistente() throws Exception {
        Mockito.when(usuarioService.actualizarUsuario(eq(1L), any(DatosUsuario.class)))
                .thenReturn(ResponseEntity.ok("Usuario actualizado con éxito"));

        mockMvc.perform(put("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "nombre": "NuevoNombre",
                        "correo_electronico": "nuevo@correo.com",
                        "contrasena": "nuevaContrasena",
                        "perfil": "admin"
                    }
                """))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario actualizado con éxito"));
    }

    @Test
    void crearUsuario_creaNuevoUsuario() throws Exception {
        Mockito.when(usuarioService.crearUsuario(any(DatosUsuario.class)))
                .thenReturn(datosUsuario);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "nombre": "UsuarioValido",
                        "correo_electronico": "usuario@valido.com",
                        "contrasena": "contrasena123",
                        "perfil": "usuario"
                    }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("UsuarioValido"))
                .andExpect(jsonPath("$.correo_electronico").value("usuario@valido.com"));
    }
}
