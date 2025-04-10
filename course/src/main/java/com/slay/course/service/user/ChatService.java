package com.slay.course.service.user;

import com.slay.course.DTO.request.user.MessageRequest;
import com.slay.course.DTO.response.user.ChatDTO;
import com.slay.course.DTO.response.user.ChatFirstMessageRequest;
import com.slay.course.DTO.response.user.ChatLiteDTO;
import com.slay.course.entity.user.ChatEntity;
import com.slay.course.entity.user.MessageEntity;
import com.slay.course.entity.user.UserEntity;
import com.slay.course.exception.ChatNotFoundException;
import com.slay.course.exception.FirstMessageAlreadyExistsException;
import com.slay.course.exception.UserNotFoundException;
import com.slay.course.repository.user.ChatRepo;
import com.slay.course.repository.user.MessageRepo;
import com.slay.course.repository.user.UserRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChatService {
    private final ChatRepo chatRepo;
    private final MessageRepo messageRepo;
    private final UserRepo userRepo;

    @Cacheable(value = "all-user-chats", key = "#userDetails.username")
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

    @CacheEvict(value = "all-user-chats", key = "#userDetails.username")
    public void createChatAndFirstMessage(UserDetails userDetails, ChatFirstMessageRequest chatBody) {
        UserEntity sender = userRepo.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> UserNotFoundException.builder().build()
        );
        UserEntity receiver = userRepo.findByUsername(chatBody.getReceiver()).orElseThrow(
                () -> UserNotFoundException.builder().build()
        );

        Optional<ChatEntity> existingChat = chatRepo.findChatByMembers(sender, receiver);

        if (existingChat.isEmpty() && sender.getPurchasedTrainingCourses().stream()
                .anyMatch(course -> receiver.getTrainingCourse().contains(course))
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
            message.setSender(sender);
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

        messageEntity.setSender(senderEntity);
        messageEntity.setMessage(messageRequest.getMessage());

        messageEntity.setChat(chatRepo.findById(messageRequest.getChatId()).orElse(null));

        if (messageRequest.getFiles() != null && !messageRequest.getFiles().isEmpty()) {
            messageEntity.setFiles(messageRequest.getFiles());
        }

        messageRepo.save(messageEntity);

    }
}
