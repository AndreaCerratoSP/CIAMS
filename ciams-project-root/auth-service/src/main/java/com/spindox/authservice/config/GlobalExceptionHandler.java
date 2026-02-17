package com.spindox.authservice.config;

import com.spindox.authservice.dto.MessageResponseDTO;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponseDTO> handleAllExceptions(Exception ex) {
        log.error("GlobalExceptionHandler - handleAllExceptions: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            ConstraintViolationException.class
    })
    public ResponseEntity<MessageResponseDTO> handleBadRequest(Exception ex) {
        log.warn("GlobalExceptionHandler - BadRequestException: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<MessageResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldError() != null
                ? ex.getBindingResult().getFieldError().getDefaultMessage()
                : ex.getMessage();
        log.warn("GlobalExceptionHandler - ValidationException: {}", message);
        return buildErrorResponse(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ BadCredentialsException.class })
    public ResponseEntity<MessageResponseDTO> handleBadCredentials(BadCredentialsException ex) {
        log.warn("GlobalExceptionHandler - BadCredentialsException: {}", ex.getMessage());
        return buildErrorResponse("Invalid username or password", HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<MessageResponseDTO> buildErrorResponse(String message, HttpStatus status) {
        MessageResponseDTO response = new MessageResponseDTO(message, status.value());
        return new ResponseEntity<>(response, status);
    }
}
