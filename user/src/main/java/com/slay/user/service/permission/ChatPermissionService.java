package com.slay.user.service.permission;


import com.slay.user.entity.chat.ChatEntity;
import com.slay.user.exception.ChatNotFoundException;
import com.slay.user.repository.chat.ChatRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ChatPermissionService {

    private final ChatRepo chatRepo;

    public boolean isChatMember(int chatId, int userId) {

        ChatEntity chat = chatRepo.findById(chatId)
                .orElseThrow(() -> ChatNotFoundException.builder().build());

        return chat.getMembers()
                .stream()
                .anyMatch(member -> member.equals(userId));
    }


}