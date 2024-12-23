package covacha.foro.controller;

import covacha.foro.domain.respuesta.Respuesta;
import covacha.foro.domain.respuesta.dto.DatosRegistrarRespuesta;
import covacha.foro.service.respuesta.RespuestaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RespuestaController.class) // Indica que probamos el controlador RespuestaController
class RespuestaControllerTest {

    @Autowired
    private MockMvc mockMvc; // Simulador de solicitudes HTTP

    @MockBean
    private RespuestaService respuestaService; // Mock del servicio utilizado por el controlador

    @Test
    void agregarRespuesta_debeRetornarRespuestaCreada() throws Exception {
        // Datos de entrada simulados
        var datosRegistrarRespuesta = new DatosRegistrarRespuesta(1L, "Este es el mensaje de prueba");

        // Respuesta esperada del servicio
        var respuesta = new Respuesta();
        respuesta.setId(1L);
        respuesta.setMensaje("Este es el mensaje de prueba");
        respuesta.setUsuarioQueRespondio("UsuarioPrueba");

        // Configuración del mock del servicio
        Mockito.when(respuestaService.agregarRespuesta(Mockito.any(DatosRegistrarRespuesta.class)))
                .thenReturn(respuesta);

        // Realizar la solicitud simulada
        mockMvc.perform(MockMvcRequestBuilders.post("/respuesta") // Solicitud POST al endpoint
                        .contentType(MediaType.APPLICATION_JSON) // Tipo de contenido: JSON
                        .content("""
                            {
                                "idTopico": 1, 
                                "mensaje": "Este es el mensaje de prueba"
                            }
                        """))
                .andExpect(MockMvcResultMatchers.status().isCreated()) // Código HTTP esperado: 201 Created
                .andExpect(MockMvcResultMatchers.header().string("Location", "/respuesta/1")) // Verifica el encabezado Location
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1)) // Verifica que la respuesta contiene "id": 1
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensaje").value("Este es el mensaje de prueba")) // Verifica "mensaje"
                .andExpect(MockMvcResultMatchers.jsonPath("$.usuarioQueRespondio").value("UsuarioPrueba")); // Verifica "usuarioQueRespondio"
    }
}
