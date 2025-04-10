package com.slay.course.DTO.response.user;

import com.slay.course.entity.user.ChatEntity;
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
