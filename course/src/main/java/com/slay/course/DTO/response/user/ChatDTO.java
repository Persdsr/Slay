package com.slay.course.DTO.response.user;

import com.slay.course.entity.user.ChatEntity;
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
        dto.setMembers(chat.getMembers().stream().map(UserLiteDTO::toModel).collect(Collectors.toSet()));
        return dto;
    }
}
