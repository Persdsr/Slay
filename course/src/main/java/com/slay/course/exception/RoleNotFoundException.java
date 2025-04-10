package com.slay.course.exception;

import com.slay.course.enums.Code;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class RoleNotFoundException extends RuntimeException {

    private final Code code;
    private final String message;
    private final HttpStatus httpStatus;
}
