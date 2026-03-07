package paulodev.orderflowapi.web.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import paulodev.orderflowapi.exception.*;
import org.springframework.security.core.AuthenticationException;
import paulodev.orderflowapi.web.exception.ConflictException;
import paulodev.orderflowapi.web.exception.ForbiddenOperationException;
import paulodev.orderflowapi.web.exception.InvalidOperationException;
import paulodev.orderflowapi.web.exception.ResourceNotFoundException;

import java.time.Instant;

// @RestControllerAdvice usada para tratar exceções de forma global, captura
// todas as exceções lançadas pelos controllers e trata nessa classe.

@RestControllerAdvice
public class GlobalExceptionHandler {

    /// user not found / order not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> resourceNotFound (
            ResourceNotFoundException exception,
            HttpServletRequest request
    ) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                404,
                "NOT_FOUND",
                exception.getMessage(),
                request.getRequestURI(),
                Instant.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /// cancelar pedido de outro usuário / update dos dados de outro usuário / deletar outro usuário
    @ExceptionHandler(ForbiddenOperationException.class)
    public ResponseEntity<ErrorResponseDTO> forbiddenOperation (
            ForbiddenOperationException exception,
            HttpServletRequest request
    ) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                403,
                "FORBIDDEN",
                exception.getMessage(),
                request.getRequestURI(),
                Instant.now());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    /// cancelar pedido ja cancelado/concluído
    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ErrorResponseDTO> invalidOperation (
            InvalidOperationException exception,
            HttpServletRequest request
    ) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                400,
                "BAD_REQUEST",
                exception.getMessage(),
                request.getRequestURI(),
                Instant.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /// usuário ja foi criado com aquele dado (email por exemplo)
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDTO> conflict (
            ConflictException exception,
            HttpServletRequest request
    ) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                409,
                "CONFLICT",
                exception.getMessage(),
                request.getRequestURI(),
                Instant.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    ///  usuario ou senha errado no login / Token
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDTO> userNotAuthenticated (
            AuthenticationException exception,
            HttpServletRequest request
    ) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                401,
                "UNAUTHORIZED",
                "Credenciais inválidas",
                request.getRequestURI(),
                Instant.now());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
}
