package com.slay.course.service.permission;

import com.slay.course.entity.user.ChatEntity;
import com.slay.course.exception.ChatNotFoundException;
import com.slay.course.repository.user.ChatRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ChatPermissionService {

    private final ChatRepo chatRepo;

    public boolean isChatMember(int chatId, String username) {

        ChatEntity chat = chatRepo.findById(chatId)
                .orElseThrow(() -> ChatNotFoundException.builder().build());

        return chat.getMembers()
                .stream()
                .anyMatch(member -> member.getUsername().equals(username));
    }


}