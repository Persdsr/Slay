package com.slay.user.exception;

import com.slay.user.enums.Code;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Исключение, выбрасываемое при попытке регистрации с уже существующим в системе username.
 */
@Data
@Builder
public class UsernameAlreadyExistsException extends RuntimeException {
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