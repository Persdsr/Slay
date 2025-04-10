package com.slay.course.handler;

import com.slay.course.enums.Code;
import com.slay.course.exception.*;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
@Hidden
public class PhraseServiceErrorHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.NOT_FOUND).message("Username not found").build()).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoleNotFoundException(RoleNotFoundException ex) {
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.NOT_FOUND).message("Role not found").build()).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.NOT_FOUND).message("Category not found").build()).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReviewNotFoundException(ReviewNotFoundException ex) {
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.NOT_FOUND).message("Review not found").build()).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleIdNotFoundException() {
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.NOT_FOUND).message("User not found").build()).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TrainingCourseNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTrainingCourseNameNotFoundException()  {
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.NOT_FOUND).message("Training course name not found").build()).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException()  {
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.ACCESS_DENIED).message("Access denied").build()).build(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidChatMemberException.class)
    public ResponseEntity<ErrorResponse> handleChatMemberException()  {
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

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEmailNotFoundException()  {
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.NOT_FOUND).message("Email not found").build()).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FirstMessageAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleFirstMessageInChatException()  {
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.ACCESS_DENIED).message("First message in chat already exists").build()).build(), HttpStatus.BAD_REQUEST);
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

