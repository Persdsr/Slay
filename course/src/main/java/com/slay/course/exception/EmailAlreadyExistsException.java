package com.slay.course.exception;

import com.slay.course.enums.Code;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Исключение, выбрасываемое при попытке регистрации с уже существующим в системе email.
 */
@Data
@Builder
public class EmailAlreadyExistsException extends RuntimeException {
    /**
     * Код ошибки, связанный с исключением.
     */
    private final Code code;

    /**
     * Сообщение об ошибке, описывающее причину исключения.
     */
    private final String message;

    /**
     * HTTP-статус, который будет возвращен в ответе.
     */
    private final HttpStatus httpStatus;
}
