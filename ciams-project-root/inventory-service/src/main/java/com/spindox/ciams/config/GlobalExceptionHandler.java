package com.spindox.ciams.config;

import com.spindox.ciams.dto.MessageResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponseDTO> handleAllExceptions(Exception ex) throws Exception {
        log.error("GlobalExceptionHandler - handleAllExceptions: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ BadRequestException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            ConstraintViolationException.class,
            EmptyResultDataAccessException.class,
            NoSuchElementException.class,
            IllegalArgumentException.class})
    public ResponseEntity<MessageResponseDTO> handleBadRequestException(Exception ex) throws Exception {
        log.warn("GlobalExceptionHandler - BadRequestException: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class, BindException.class })
    public ResponseEntity<MessageResponseDTO> handleValidationExceptions(Exception ex) throws Exception {
        String message = ex.getMessage();
        if (ex instanceof MethodArgumentNotValidException manv && manv.getBindingResult().getFieldError() != null) {
            message = manv.getBindingResult().getFieldError().getDefaultMessage();
        }
        if (ex instanceof BindException be && be.getBindingResult().getFieldError() != null) {
            message = be.getBindingResult().getFieldError().getDefaultMessage();
        }
        log.warn("GlobalExceptionHandler - ValidationException: {}", message);
        return buildErrorResponse(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<MessageResponseDTO> handleDataNotFoundException(Exception ex) throws Exception {
        log.warn("GlobalExceptionHandler - DataNotFoundException: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ DataIntegrityViolationException.class })
    public ResponseEntity<MessageResponseDTO> handleConflict(Exception ex) throws Exception {
        log.warn("GlobalExceptionHandler - Conflict: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ AuthenticationException.class })
    public ResponseEntity<MessageResponseDTO> handleUnauthorized(Exception ex) throws Exception {
        log.warn("GlobalExceptionHandler - Unauthorized: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<MessageResponseDTO> handleForbidden(Exception ex) throws Exception {
        log.warn("GlobalExceptionHandler - Forbidden: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<MessageResponseDTO> buildErrorResponse(String message, HttpStatus status) throws Exception {
        MessageResponseDTO response = new MessageResponseDTO(message, status.value());
        return new ResponseEntity<MessageResponseDTO>(response, status);
    }

}
