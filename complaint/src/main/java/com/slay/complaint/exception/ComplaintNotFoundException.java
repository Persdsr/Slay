package com.slay.complaint.exception;

import com.slay.complaint.enums.Code;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ComplaintNotFoundException extends RuntimeException {
    private final Code code;
    private final String message;
    private final HttpStatus httpStatus;
}
