package com.augustin.gabriel.goldenraspberryawardsapi.controllers.advices;

import com.augustin.gabriel.goldenraspberryawardsapi.dtos.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Invalid argument: {}", e.getMessage());
        
        ErrorResponseDto error = new ErrorResponseDto(
            "Invalid parameter",
            e.getMessage(),
            "400"
        );
        
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.error("Parameter type mismatch: {} = {}", e.getName(), e.getValue());
        
        ErrorResponseDto error = new ErrorResponseDto(
            "Parameter type mismatch",
            String.format("Parameter '%s' with value '%s' has invalid type", e.getName(), e.getValue()),
            "400"
        );
        
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(org.springframework.dao.DataAccessException.class)
    public ResponseEntity<ErrorResponseDto> handleDataAccessException(org.springframework.dao.DataAccessException e) {
        log.error("Database error: {}", e.getMessage());
        
        ErrorResponseDto error = new ErrorResponseDto(
            "Database error",
            "An error occurred while accessing the database",
            "500"
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception e) {
        log.error("Unexpected error: {}", e.getMessage(), e);
        
        ErrorResponseDto error = new ErrorResponseDto(
            "Internal server error",
            "An unexpected error occurred",
            "500"
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
} 
