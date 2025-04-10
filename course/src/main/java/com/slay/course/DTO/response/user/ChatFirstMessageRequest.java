package com.slay.course.DTO.response.user;

import lombok.Data;

@Data
public class ChatFirstMessageRequest {
    private String receiver;
    private String message;
}