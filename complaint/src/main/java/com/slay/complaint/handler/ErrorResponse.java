package com.slay.complaint.handler;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private Error error;
}
