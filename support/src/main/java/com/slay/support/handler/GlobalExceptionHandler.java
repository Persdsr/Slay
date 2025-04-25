package com.slay.support.handler;

import com.slay.support.enums.Code;
import com.slay.support.exception.SupportResolvedException;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
@Hidden
public class GlobalExceptionHandler {

    /*@ExceptionHandler(SupportResolvedException.class)
    public ResponseEntity<ErrorResponse> handleSupportResolvedException(SupportResolvedException ex) {
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.ACCESS_DENIED).message("The support has already been closed").build()).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
*/
}