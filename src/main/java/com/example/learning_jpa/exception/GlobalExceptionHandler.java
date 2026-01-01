package com.example.learning_jpa.exception;

import com.example.learning_jpa.dto.GeneralResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Responds to validation errors with unauthorized status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GeneralResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        GeneralResponseDto response = new GeneralResponseDto();
        response.setRes(false);
        response.setMsg("Your JWT token has expired. Please log in again.");
        response.setData(null);
        response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Handles illegal argument exceptions; returns bad request
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GeneralResponseDto> handleIllegalArg(IllegalArgumentException ex) {
        GeneralResponseDto response = new GeneralResponseDto();
        response.setRes(false);
        response.setMsg(ex.getMessage());
        response.setData(null);
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
