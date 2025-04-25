package com.slay.user.dto.response.chat;

import com.slay.user.entity.chat.MessageEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MessageDTO {
    private Long id;
    private String message;
    //private UserLiteDTO sender;
    private List<String> files;
    private LocalDateTime createdAt;

    public static MessageDTO toModel(MessageEntity messageEntity) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessage(messageEntity.getMessage());
        if (messageDTO.getMessage() != null) {
            messageDTO.setFiles(messageEntity.getFiles());
        }

        //messageDTO.setSender(UserLiteDTO.toModel(messageEntity.getSender()));
        messageDTO.setCreatedAt(messageEntity.getCreatedAt());
        messageDTO.setId(messageEntity.getId());
        return messageDTO;
    }
}
