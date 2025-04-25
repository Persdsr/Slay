package com.slay.user.dto.response.chat;

import com.slay.user.dto.response.user.UserLiteDTO;
import com.slay.user.entity.chat.ChatEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ChatDTO {
    private int id;
    private Set<UserLiteDTO> members;
    private List<MessageDTO> messages;
    private LocalDateTime createdAt;

    public static ChatDTO toModel(ChatEntity chat) {
        ChatDTO dto = new ChatDTO();
        dto.setId(chat.getId());
        dto.setCreatedAt(chat.getCreatedAt());
        dto.setMessages(chat.getMessages().stream().map(MessageDTO::toModel).collect(Collectors.toList()));
        return dto;
    }
}
