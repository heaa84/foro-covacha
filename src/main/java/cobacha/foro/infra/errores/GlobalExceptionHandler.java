package cobacha.foro.infra.errores;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<TratadorDeErrores> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Obtener el primer error de validación
        FieldError fieldError = ex.getBindingResult().getFieldErrors().get(0);

        // Construir el mensaje a partir del defaultMessage
        String mensaje = fieldError.getDefaultMessage();

        // Usar TratadorDeErrores para devolver el mensaje
        TratadorDeErrores errorResponse = new TratadorDeErrores(mensaje);

        return ResponseEntity.badRequest().body(errorResponse);
    }
}
