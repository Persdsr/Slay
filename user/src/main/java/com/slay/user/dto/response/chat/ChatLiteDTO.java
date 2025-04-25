package com.slay.user.dto.response.chat;

import com.slay.user.dto.response.user.UserLiteDTO;
import com.slay.user.entity.chat.ChatEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ChatLiteDTO {
    private int id;
    private Set<UserLiteDTO> members;
    private String lastMessage;
    private LocalDateTime createdAt;

    public static ChatLiteDTO toModel(ChatEntity chat) {
        ChatLiteDTO dto = new ChatLiteDTO();
        dto.setId(chat.getId());
        dto.setCreatedAt(chat.getCreatedAt());
        if (!chat.getMessages().isEmpty()) {
            dto.setLastMessage(chat.getMessages().get(chat.getMessages().size() - 1).getMessage());
        }
        dto.setMembers(chat.getMembers().stream().map(UserLiteDTO::toModel).collect(Collectors.toSet()));
        return dto;
    }
}
