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


    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.NOT_FOUND).message("Category not found").build()).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReviewNotFoundException(ReviewNotFoundException ex) {
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.NOT_FOUND).message("Review not found").build()).build(), HttpStatus.NOT_FOUND);
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

}

