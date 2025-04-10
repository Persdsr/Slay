package com.slay.course.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.slay.course.enums.Code;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Error {

    private Code code;
    private String message;
}