package com.slay.support.controller;

import com.slay.support.dto.request.support.SupportAcceptRequest;
import com.slay.support.dto.request.support.SupportMessageRequest;
import com.slay.support.dto.response.support.SupportRequestDTO;
import com.slay.support.enums.SupportRequestType;
import com.slay.support.security.UserDetailsImpl;
import com.slay.support.service.SupportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@RestController
@RequestMapping("/api/support")
@AllArgsConstructor
@Tag(name = "Support", description = "Поддержка")
public class SupportController {

    private final SupportService supportService;
    private final SimpMessagingTemplate messagingTemplate;

    @Operation(
            summary = "Получить типы запросов в поддержку",
            description = "Возвращает список типов запросов в поддержку с локализованными названиями. Локализация зависит от языка, указанного в заголовке запроса (например, `Accept-Language`)."
    )
    @GetMapping(value = "/support-request-types", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<String>> getSupportRequestTypes() {
        Locale locale = LocaleContextHolder.getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        return new ResponseEntity<>(Arrays.stream(SupportRequestType.values())
                .map(type -> bundle.getString(type.name()))
                .toList(), HttpStatus.OK);
    }

    @Operation(
            summary = "Получить все запросы в поддержку",
            description = "Возвращает список всех запросов в поддержку. Доступ к этому методу ограничен администраторами и модераторами."
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @GetMapping()
    public ResponseEntity<List<SupportRequestDTO>> getAllSupports() {
        return new ResponseEntity<>(supportService.getAllSupportRequests(), HttpStatus.OK);
    }

    @Operation(
            summary = "Получить все запросы в поддержку для конкретного пользователя",
            description = "Возвращает список всех запросов в поддержку, отправленных указанным пользователем. Доступ к этому методу ограничен:" +
        "- Администраторы (`ADMIN`)." +
        "- Модераторы (`MODERATOR`)." +
        "- Сам пользователь (только для своих запросов)."
    )
    //@PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR') or @supportPermissionService.isSupportSender(#senderId, #userDetails.id)")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SupportRequestDTO>> getAllUserSupportRequests(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable("userId") int senderId) {
        return new ResponseEntity<>(supportService.getAllUserSupportRequests(senderId), HttpStatus.OK);
    }

    @Operation(
            summary = "Получить детали запроса в поддержку",
            description = "Возвращает детали запроса в поддержку по его идентификатору. Доступ к этому методу ограничен:" +
        "- Администраторы (`ADMIN`)." +
        "- Модераторы (`MODERATOR`)." +
        "- Пользователь, который отправил запрос."
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR') or @supportPermissionService.isSupportSender(#supportId, #userDetails.id)")
    @GetMapping("/{supportId}")
    public ResponseEntity<SupportRequestDTO> getSupportDetail(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable("supportId") int supportId) {
        return new ResponseEntity<>(supportService.getSupportDetailById(supportId), HttpStatus.OK);
    }

    @Operation(
            summary = "Удалить запрос в поддержку",
            description = "Удаляет запрос в поддержку по его идентификатору. Доступ к этому методу ограничен:" +
        "- Администраторы (`ADMIN`)." +
        "- Модераторы (`MODERATOR`)."
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @DeleteMapping("/{supportId}")
    public ResponseEntity<HttpStatus> deleteSupport(@PathVariable("supportId") int supportId) {
        supportService.deleteSupport(supportId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Создать новый запрос в поддержку",
            description = "Создает новый запрос в поддержку с указанными данными и прикреплёнными изображениями. Доступ к этому методу ограничен авторизованными пользователями. "
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Отзыв успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные в запросе"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
            @ApiResponse(responseCode = "404", description = "Курс с указанным ID не найден"),
            @ApiResponse(responseCode = "415", description = "Unsupported Media Type", content = @Content(
                    examples = @ExampleObject(value = "{\n" +
                            "  \"timestamp\": \"2025-03-12T13:39:23.322+00:00\",\n" +
                            "  \"status\": 415,\n" +
                            "  \"error\": \"Unsupported Media Type\",\n" +
                            "  \"path\": \"/api/course-course/review\"\n" +
                            "}")
            ))
    })
    @PreAuthorize("isAuthenticated()")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpStatus> handlePostSupportRequest(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "Данные тикета", required = true,
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
            @RequestPart("supportBody") SupportAcceptRequest supportRequest,

            @Parameter(description = "Изображения отзыва")
            @RequestPart(value = "images", required = false) MultipartFile[] images
                                                    ) {

        supportService.createSupportRequest(userDetails.getId(), supportRequest, images);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Изменить статус разрешения запроса в поддержку",
            description = "Изменяет статус разрешения запроса в поддержку на противоположный (с `решено` на `не решено` и наоборот). Доступ к этому методу ограничен:"
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @PatchMapping("{supportId}")
    public ResponseEntity<HttpStatus> handleChangeSupportResolvedStatus(@PathVariable("supportId") int supportId) {
        supportService.changeSupportResolvedStatus(supportId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Отправить сообщение в чат",
            description = "Отправляет сообщение в чат через WebSocket. Сообщение сохраняется в базе данных и отправляется всем подписанным на тему `/topic/chat/{chatId}` клиентам."
    )
    @MessageMapping("/chat.sendMessage")
    public void sendSupportMessage(SupportMessageRequest message) {
        supportService.createSupportMessageWS(message);
        String topic = "/topic/support/" + message.getSupportRequestId();
        messagingTemplate.convertAndSend(topic, message);
    }


}
