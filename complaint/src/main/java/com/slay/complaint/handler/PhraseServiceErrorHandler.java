package com.slay.complaint.handler;

import com.slay.complaint.enums.Code;
import com.slay.complaint.exception.AccessDeniedException;
import com.slay.complaint.exception.InvalidJsonException;
import com.slay.complaint.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@ControllerAdvice
@Hidden
public class PhraseServiceErrorHandler {


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException()  {
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.ACCESS_DENIED).message("Access denied").build()).build(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException()  {
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.NOT_FOUND).message("Not found").build()).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidJsonException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJsonException()  {
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.INVALID_JSON).message("Invalid json").build()).build(), HttpStatus.BAD_REQUEST);
    }

}

