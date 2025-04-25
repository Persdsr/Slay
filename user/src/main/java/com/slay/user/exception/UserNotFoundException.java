package com.slay.user.exception;

import com.slay.user.enums.Code;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class UserNotFoundException extends RuntimeException {

    private final Code code;
    private final String message;
    private final HttpStatus httpStatus;
}