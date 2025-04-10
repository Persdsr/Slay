package com.slay.course.handler;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private Error error;
}
