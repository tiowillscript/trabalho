package br.com.baozi.controller;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
@RestControllerAdvice
public class ApiExceptionHandler {
    private ResponseEntity<Map<String, Object>> erro(int status, String mensagem, String caminho) {
        return ResponseEntity.status(status).body(Map.of("status", status, "mensagem", mensagem, "caminho", caminho));
    }
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> tratar(ResponseStatusException e, HttpServletRequest r) {
        return erro(e.getStatusCode().value(), Objects.requireNonNullElse(e.getReason(), "Requisição inválida"), r.getRequestURI());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validar(MethodArgumentNotValidException e, HttpServletRequest r) {
        String campos = e.getBindingResult().getFieldErrors().stream().map(f -> f.getField()).distinct().sorted().reduce((a,b) -> a + ", " + b).orElse("corpo");
        return erro(400, "Campos inválidos: " + campos, r.getRequestURI());
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> json(HttpMessageNotReadableException e, HttpServletRequest r) {
        return erro(400, "JSON inválido ou tipo de campo incorreto", r.getRequestURI());
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> integridade(DataIntegrityViolationException e, HttpServletRequest r) {
        return erro(409, "Registro vinculado a outro recurso", r.getRequestURI());
    }
}
