package isi.dan.msclientes.exception;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.micrometer.core.instrument.config.validate.ValidationException;

@ControllerAdvice
public class RestControllerException {

    private static final Logger logger = LoggerFactory.getLogger(RestControllerException.class);

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleUserNotFoundException(UserNotFoundException ex) {
        logger.error("ERROR Buscando User", ex);
        String detalle = ex.getCause() == null ? "User no encontrado." : ex.getCause().getMessage();

        return new ResponseEntity<ErrorInfo>(
                new ErrorInfo(Instant.now(), ex.getMessage(), detalle, HttpStatus.NOT_FOUND.value()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorInfo> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        logger.error("ERROR Autenticando User", ex);
        String detalle = ex.getCause() == null ? "La contraseña es inválida." : ex.getCause().getMessage();

        return new ResponseEntity<ErrorInfo>(
                new ErrorInfo(Instant.now(), ex.getMessage(), detalle, HttpStatus.UNAUTHORIZED.value()),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorInfo> handleIllegalStateException(IllegalStateException ex) {
        logger.error("ERROR de negocio", ex);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorInfo(Instant.now(), ex.getMessage(), "No se puede procesar la solicitud.",
                        HttpStatus.CONFLICT.value()));
    }

    @ExceptionHandler(ClienteNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleClientNotFoundException(ClienteNotFoundException ex) {
        logger.error("ERROR Buscando Cliente", ex);
        String detalle = ex.getCause() == null ? "Cliente no encontrado." : ex.getCause().getMessage();

        return new ResponseEntity<ErrorInfo>(
                new ErrorInfo(Instant.now(), ex.getMessage(), detalle, HttpStatus.NOT_FOUND.value()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ObraNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleObraNotFoundException(ObraNotFoundException ex) {
        logger.error("ERROR Buscando Obra", ex);
        String detalle = ex.getCause() == null ? "Obra no encontrada." : ex.getCause().getMessage();

        return new ResponseEntity<ErrorInfo>(
                new ErrorInfo(Instant.now(), ex.getMessage(), detalle, HttpStatus.NOT_FOUND.value()),
                HttpStatus.NOT_FOUND);
    }

    // TODO actually just get the exception class
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorInfo> handleConstraintViolationException(ValidationException ex) {
        logger.error("ERROR Validando Cliente", ex);
        String detalle = ex.getMessage();

        return new ResponseEntity<ErrorInfo>(
                new ErrorInfo(Instant.now(), ex.getMessage(), detalle, HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> handleOtherExceptions(Exception ex) {
        logger.error("ERROR MS CLIENTES", ex);
        String detalle = ex.getCause() == null ? "Error en el servidor." : ex.getCause().getMessage();
        return new ResponseEntity<ErrorInfo>(
                new ErrorInfo(Instant.now(), ex.getMessage(), detalle, HttpStatus.INTERNAL_SERVER_ERROR.value()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
