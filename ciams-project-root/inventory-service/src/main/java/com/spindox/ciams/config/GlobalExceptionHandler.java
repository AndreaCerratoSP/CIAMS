package com.spindox.ciams.config;

import com.spindox.ciams.dto.MessageResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponseDTO> handleAllExceptions(Exception ex) throws Exception {
        log.info("GlobalExceptionHandler - handleAllExceptions: "+ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    //400
    @ExceptionHandler({ BadRequestException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            ConstraintViolationException.class,
            EmptyResultDataAccessException.class,
            NoSuchElementException.class})
    public ResponseEntity<MessageResponseDTO> handleBadRequestException(Exception ex) throws Exception {
        log.info("GlobalExceptionHandler - BadRequestException: "+ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    //404
    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<MessageResponseDTO> handleDataNotFoundException(Exception ex) throws Exception {
        log.info("GlobalExceptionHandler - DataNotFoundException: "+ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<MessageResponseDTO> buildErrorResponse(String message, HttpStatus status) throws Exception {
        MessageResponseDTO response = new MessageResponseDTO(message, status.value());
        return new ResponseEntity<MessageResponseDTO>(response, status);
    }

}