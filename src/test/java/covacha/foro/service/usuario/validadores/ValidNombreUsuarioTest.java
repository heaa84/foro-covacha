package covacha.foro.service.usuario.validadores;

import covacha.foro.domain.usuario.dto.DatosUsuario;
import covacha.foro.service.ValidacionExcepcion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidNombreUsuarioTest {

    private ValidNombreUsuario validador;

    @BeforeEach
    void setUp() {
        validador = new ValidNombreUsuario();
    }

    @Test
    void validar_nombreValido_noLanzaExcepcion() {
        DatosUsuario datos = new DatosUsuario(
                "UsuarioValido",
                "usuario@valido.com",
                "contrasena123",
                "usuario"
        );
        assertDoesNotThrow(() -> validador.validar(datos));
    }

    @Test
    void validar_nombreCorto_lanzaValidacionExcepcion() {
        DatosUsuario datos = new DatosUsuario(
                "Abc",
                "usuario@valido.com",
                "contrasena123",
                "usuario"
        );
        ValidacionExcepcion excepcion = assertThrows(
                ValidacionExcepcion.class,
                () -> validador.validar(datos)
        );
        assertEquals("Error mensaje muy corto o nulo", excepcion.getMessage());
    }

    @Test
    void validar_nombreVacio_lanzaValidacionExcepcion() {
        DatosUsuario datos = new DatosUsuario(
                "",
                "usuario@valido.com",
                "contrasena123",
                "usuario"
        );
        ValidacionExcepcion excepcion = assertThrows(
                ValidacionExcepcion.class,
                () -> validador.validar(datos)
        );
        assertEquals("Error mensaje muy corto o nulo", excepcion.getMessage());
    }

    @Test
    void validar_nombreNulo_lanzaNullPointerException() {
        DatosUsuario datos = new DatosUsuario(
                null,
                "usuario@valido.com",
                "contrasena123",
                "usuario"
        );
        assertThrows(NullPointerException.class, () -> validador.validar(datos));
    }
}
