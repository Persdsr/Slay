package com.slay.user.handler;

import com.slay.user.enums.Code;
import com.slay.user.exception.*;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
@Hidden
public class PhraseServiceErrorHandler {

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoleNotFoundException(RoleNotFoundException ex) {
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.NOT_FOUND).message("Role not found").build()).build(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleIdNotFoundException() {
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.NOT_FOUND).message("User not found").build()).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEmailNotFoundException()  {
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.NOT_FOUND).message("Email not found").build()).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FirstMessageAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleFirstMessageInChatException()  {
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.ACCESS_DENIED).message("First message in chat already exists").build()).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ChatNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleChatNotFoundException(ChatNotFoundException ex) {
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.NOT_FOUND).message("Chat not found").build()).build(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUsernameAlreadyExistsException()  {
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.NOT_FOUND).message("Username already exists").build()).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException()  {
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.NOT_FOUND).message("Email already exists").build()).build(), HttpStatus.NOT_FOUND);
    }


}

