package covacha.foro.controller;

import covacha.foro.domain.respuesta.Respuesta;
import covacha.foro.domain.respuesta.dto.DatosRegistrarRespuesta;
import covacha.foro.service.respuesta.RespuestaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RespuestaControllerTest {

    @Mock
    private RespuestaService respuestaService;

    @InjectMocks
    private RespuestaController respuestaController;

    @Test
    void agregarRespuesta_validData_returnsCreated() {
        // Aquí va tu código para probar con datos válidos
        DatosRegistrarRespuesta datosValidos= new DatosRegistrarRespuesta(1L, "Esta es una respuesta válida y completa.");
        Respuesta respuestaMock=new Respuesta();
        respuestaMock.setId(1L);
        when(respuestaService.agregarRespuesta(datosValidos)).thenReturn(respuestaMock);

        ResponseEntity<?> response = respuestaController.agregarRespuesta(datosValidos);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
// Puedes verificar la URL si lo deseas, pero eso requiere más configuración
    }


}