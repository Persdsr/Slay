package com.slay.course.DTO.request.support;

import com.slay.course.enums.SupportRequestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SupportAcceptRequest {

    @Schema(description = "email отправителя", example = "John2004@mail.ru")
    private String email;

    @Schema(description = "Тема запроса", example = "Украли акк")
    private String subject;

    @Schema(description = "Описание запроса", example = "Помогите..")
    private String description;

    @Schema(description = "Тип запроса", example = "TECHNICAL_SUPPORT")
    private SupportRequestType requestType;

}
