package com.slay.support.handler;

import lombok.Builder;
import lombok.Data;

import java.lang.Error;

@Data
@Builder
public class ErrorResponse {
    private Error error;
}
