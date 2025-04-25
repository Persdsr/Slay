package com.slay.user.dto.response.chat;

import lombok.Data;

@Data
public class ChatFirstMessageRequest {
    private String receiver;
    private String message;
}