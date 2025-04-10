package com.slay.course.controller.user;

import com.slay.course.DTO.request.user.MessageRequest;
import com.slay.course.DTO.response.user.ChatDTO;
import com.slay.course.DTO.response.user.ChatFirstMessageRequest;
import com.slay.course.DTO.response.user.ChatLiteDTO;
import com.slay.course.entity.user.UserEntity;
import com.slay.course.exception.UserNotFoundException;
import com.slay.course.repository.user.UserRepo;
import com.slay.course.service.user.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@AllArgsConstructor
@Tag(name = "Chat", description = "Пользовательский чат")
public class ChatController {
    private final ChatService chatService;
    private final UserRepo userRepo;
    private final SimpMessagingTemplate messagingTemplate;

    @Operation(
            summary = "Получить список чатов пользователя",
            description ="Возвращает список всех чатов, в которых участвует указанный пользователь. Доступ к этому методу ограничен:" +
                    "- Администраторы (`ADMIN`)." +
                    "- Модераторы (`MODERATOR`)." +
                    "- Сам пользователь (только для своих чатов)."
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping()
    public ResponseEntity<List<ChatLiteDTO>> getUserChats(@AuthenticationPrincipal UserDetails user) {
        return new ResponseEntity<>(chatService.getAllUserChats(user), HttpStatus.OK);
    }

    @Operation(
            summary = "Получить детали чата",
            description = "Возвращает детали чата по его идентификатору. Доступ к этому методу ограничен участниками чата, администраторами и модераторами."
    )
    @PreAuthorize("@chatPermissionService.isChatMember(#chatId, principal.username)")
    @GetMapping("/c/{chatId}")
    public ResponseEntity<ChatDTO> getChatDetail(@PathVariable("chatId") int chatId) {
        return new ResponseEntity<>(chatService.getChatDetailById(chatId), HttpStatus.OK);
    }

    @Operation(
            summary = "Создать новый чат и отправить первое сообщение",
            description = "Создает новый чат между текущим пользователем и получателем, а также отправляет первое сообщение в этот чат." +
        "Чат создается только в том случае, если текущий пользователь и получатель имеют общий тренировочный курс, и если чат между ними еще не существует."
    )
    @PreAuthorize("isAuthenticated()")
    @PostMapping()
    public ResponseEntity<HttpStatus> handleCreateChat(@AuthenticationPrincipal UserDetails userDetails,
                                                       @RequestBody ChatFirstMessageRequest chatBody) {
        chatService.createChatAndFirstMessage(userDetails, chatBody);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Отправить сообщение в чат",
            description = "Отправляет сообщение в чат через WebSocket. Сообщение сохраняется в базе данных и отправляется всем подписанным на тему `/topic/chat/{chatId}` клиентам."
    )
    @MessageMapping("/chat.sendUserMessage")
    public void sendMessage(MessageRequest message) {
        chatService.createMessageWS(message);
        UserEntity user = userRepo.findByUsername(message.getSender().getUsername()).orElseThrow(
                () -> UserNotFoundException.builder().build()
        );
        message.getSender().setAvatar(user.getAvatar());

        String topic = "/topic/chat/" + message.getChatId();
        messagingTemplate.convertAndSend(topic, message);
    }
}
