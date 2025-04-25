package com.slay.user.dto.request.chat;

import com.slay.user.dto.response.user.UserLiteDTO;
import com.slay.user.entity.chat.MessageEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MessageRequest {
    private Long id;
    private String message;
    private UserLiteDTO sender;
    private List<String> files;
    private LocalDateTime createdAt;
    private int chatId;

    public static MessageRequest toModel(MessageEntity messageEntity) {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setMessage(messageEntity.getMessage());
        if (messageRequest.getFiles() != null) {
            messageRequest.setFiles(messageEntity.getFiles());
        }

        messageRequest.setChatId(messageEntity.getChat().getId());
        messageRequest.setId(messageEntity.getId());
        return messageRequest;
    }

}
