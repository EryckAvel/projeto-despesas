package com.eryckavel.exceptions;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(Exception ex, HttpServletRequest request) {
        log.warn("Recurso não encontrado em {}: {}", request.getRequestURI(), ex.getMessage());
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler({EntityExistsException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException ex, HttpServletRequest request) {
        log.warn("Requisição inválida em {}: {}", request.getRequestURI(), ex.getMessage());
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler({LoginException.class})
    public ResponseEntity<ErrorResponse> handleUnauthorized(RuntimeException ex, HttpServletRequest request) {
        log.warn("Acesso não autorizado em {}: {}", request.getRequestURI(), ex.getMessage());
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {
        log.error("Erro inesperado em {}", request.getRequestURI(), ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                "Ocorreu um erro inesperado. Tente novamente mais tarde.",
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(body);
    }
}