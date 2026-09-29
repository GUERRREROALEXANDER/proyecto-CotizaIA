package com.cotizaia.api;
import com.cotizaia.service.PropuestaNoEncontrada;
import com.cotizaia.workflow.TransicionInvalida;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ManejadorErrores {
    @ExceptionHandler(PropuestaNoEncontrada.class)
    ProblemDetail noEncontrada(PropuestaNoEncontrada e) {
        return error(HttpStatus.NOT_FOUND, e.getMessage());
    }
    @ExceptionHandler(TransicionInvalida.class)
    ProblemDetail conflicto(TransicionInvalida e) {
        return error(HttpStatus.CONFLICT, e.getMessage());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail invalido(IllegalArgumentException e) {
        return error(HttpStatus.BAD_REQUEST, e.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail validacion(MethodArgumentNotValidException e) {
        ProblemDetail p = error(HttpStatus.BAD_REQUEST, "Revise los campos de la solicitud");
        p.setProperty("errores", e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage()).toList());
        return p;
    }
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    ProblemDetail formato(Exception e) {
        return error(HttpStatus.BAD_REQUEST, "JSON, tipos de datos o identificador UUID inválidos");
    }
    private ProblemDetail error(HttpStatus status, String detalle) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(status, detalle);
        p.setTitle(switch (status) {
            case BAD_REQUEST -> "Solicitud inválida";
            case NOT_FOUND -> "Propuesta no encontrada";
            case CONFLICT -> "Transición no permitida";
            default -> "Error";
        });
        return p;
    }
}
