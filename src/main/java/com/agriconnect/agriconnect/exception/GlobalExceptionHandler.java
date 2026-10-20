package com.agriconnect.agriconnect.exception;// ===== GlobalExceptionHandler.java =====
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApiException.class)
    ProblemDetail api(ApiException e) {
        return probleme(e.getStatus(), e.getCode());
    }

    @ExceptionHandler(CodeInconnuException.class)
    ProblemDetail codeInconnu(CodeInconnuException e) {
        ProblemDetail pd = probleme(HttpStatus.BAD_REQUEST, "errors.request.invalidCode");
        pd.setProperty("type", e.getType());
        return pd;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    ProblemDetail introuvable(EntityNotFoundException e) {
        return probleme(HttpStatus.NOT_FOUND, "errors.resource.notFound");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ProblemDetail conflit(DataIntegrityViolationException e) {
        log.warn("Violation d'intégrité : {}", e.getMostSpecificCause().getMessage());
        return probleme(HttpStatus.CONFLICT, "errors.data.conflict");
    }

    @ExceptionHandler(AccessDeniedException.class)
    ProblemDetail interdit(AccessDeniedException e) {
        return probleme(HttpStatus.FORBIDDEN, "errors.auth.forbidden");
    }

    /** Validation Jakarta : fields = { champ : clé de traduction du type d'erreur }. */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail pd = probleme(HttpStatus.BAD_REQUEST, "errors.validation.failed");
        Map<String, String> champs = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(f -> champs.putIfAbsent(f.getField(), "errors.fields." + f.getCode()));
        pd.setProperty("fields", champs);
        return ResponseEntity.badRequest().body(pd);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity.badRequest().body(probleme(HttpStatus.BAD_REQUEST, "errors.request.malformed"));
    }

    private static ProblemDetail probleme(HttpStatus status, String code) {
        ProblemDetail pd = ProblemDetail.forStatus(status);
        pd.setProperty("code", code);
        return pd;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ProblemDetail parametreInvalide(MethodArgumentTypeMismatchException e) {
        return probleme(HttpStatus.BAD_REQUEST, "errors.request.malformed");
    }
}