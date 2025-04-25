package com.slay.user.service.chat;

import com.slay.user.dto.request.chat.MessageRequest;
import com.slay.user.dto.response.chat.ChatDTO;
import com.slay.user.dto.response.chat.ChatFirstMessageRequest;
import com.slay.user.dto.response.chat.ChatLiteDTO;
import com.slay.user.entity.chat.ChatEntity;
import com.slay.user.entity.chat.MessageEntity;
import com.slay.user.entity.user.UserEntity;
import com.slay.user.exception.ChatNotFoundException;
import com.slay.user.exception.FirstMessageAlreadyExistsException;
import com.slay.user.exception.UserNotFoundException;
import com.slay.user.repository.UserRepo;
import com.slay.user.repository.chat.ChatRepo;
import com.slay.user.repository.chat.MessageRepo;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChatService {
    private final ChatRepo chatRepo;
    private final MessageRepo messageRepo;
    private final UserRepo userRepo;

    //@Cacheable(value = "all-user-chats", key = "#userDetails.username")
    public List<ChatLiteDTO> getAllUserChats(UserDetails userDetails) {
        UserEntity user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> UserNotFoundException.builder().build()
        );
        return chatRepo.findByMembers(user).stream().map(ChatLiteDTO::toModel).collect(Collectors.toList());
    }


    public ChatDTO getChatDetailById(int chatId) {
        ChatEntity chat = chatRepo.findById(chatId).orElseThrow(
                () -> ChatNotFoundException.builder().build()
        );

        return ChatDTO.toModel(chat);
    }

    //@CacheEvict(value = "all-user-chats", key = "#userDetails.username")
    public void createChatAndFirstMessage(UserDetails userDetails, ChatFirstMessageRequest chatBody) {
        UserEntity sender = userRepo.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> UserNotFoundException.builder().build()
        );
        UserEntity receiver = userRepo.findByUsername(chatBody.getReceiver()).orElseThrow(
                () -> UserNotFoundException.builder().build()
        );

        Optional<ChatEntity> existingChat = chatRepo.findChatByMembers(sender, receiver);

        if (existingChat.isEmpty() && sender.getPurchasedCourseIds().stream()
                .anyMatch(course -> receiver.getCoursesIds().contains(course))
        ) {
            ChatEntity chat = new ChatEntity();
            chatRepo.save(chat);


            chat.getMembers().add(sender);
            chat.getMembers().add(userRepo.findByUsername(chatBody.getReceiver()).orElseThrow(
                    () -> UserNotFoundException.builder().build()
            ));
            sender.getChats().add(chat);
            receiver.getChats().add(chat);
            userRepo.save(sender);
            userRepo.save(receiver);

            MessageEntity message = new MessageEntity();
            message.setSender(sender.getId());
            message.setMessage(chatBody.getMessage());
            message.setChat(chat);
            messageRepo.save(message);

            chat.getMessages().add(message);
        } else throw FirstMessageAlreadyExistsException.builder().build();
    }

    @Transactional
    public void createMessageWS(MessageRequest messageRequest) {
        MessageEntity messageEntity = new MessageEntity();
        UserEntity senderEntity = userRepo.findByUsername(messageRequest.getSender().getUsername()).orElseThrow(
                () -> UserNotFoundException.builder().build()
                );

        messageEntity.setSender(senderEntity.getId());
        messageEntity.setMessage(messageRequest.getMessage());

        messageEntity.setChat(chatRepo.findById(messageRequest.getChatId()).orElse(null));

        if (messageRequest.getFiles() != null && !messageRequest.getFiles().isEmpty()) {
            messageEntity.setFiles(messageRequest.getFiles());
        }

        messageRepo.save(messageEntity);
    }
}
