package com.slay.complaint.controller.complaint;

import com.slay.complaint.dto.response.complaint.ComplaintDTO;
import com.slay.complaint.enums.ComplaintType;
import com.slay.complaint.security.UserDetailsImpl;
import com.slay.complaint.service.complaint.ComplaintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/complaint")
@AllArgsConstructor
@Tag(name = "Complaint", description = "Управление жалобами")
public class ComplaintController {

    private final ComplaintService complaintService;

    @Operation(
            summary = "Получить все жалобы",
            description = "Возвращает список всех жалоб, зарегистрированных в системе. Доступ к этому методу ограничен:" +
                        "- Администраторы (`ADMIN`)." +
                        "- Модераторы (`MODERATOR`)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные успешно получены"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные в запросе"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
            @ApiResponse(responseCode = "403", description = "Access denied. You do not have permission to perform this action."),
            @ApiResponse(responseCode = "415", description = "Unsupported Media Type")
    })
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @GetMapping()
    public ResponseEntity<List<ComplaintDTO>> getAllComplaints() {
        return new ResponseEntity<>(complaintService.getAllComplaints(), HttpStatus.OK);
    }

    @Operation(
            summary = "Получить жалобу по её идентификатору",
            description = "Возвращает детали жалобы по её идентификатору. Доступ к этому методу ограничен:" +
                    "- Администраторы (`ADMIN`)." +
                    "- Модераторы (`MODERATOR`)."
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @GetMapping("/{complaintId}")
    public ResponseEntity getComplaint(
            @Parameter(description = "ID жалобы", required = true,
            content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE))
            @PathVariable int complaintId
    ) {
        return complaintService.getComplaintById(complaintId);
    }

    @Operation(
            summary = "Получить все жалобы, отправленные пользователем",
            description = "Возвращает список всех жалоб, отправленных указанным пользователем. Доступ к этому методу ограничен:" +
        "- Администраторы (`ADMIN`)." +
        "- Модераторы (`MODERATOR`)."
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @GetMapping("/user")
    public ResponseEntity<List<ComplaintDTO>> getAllUserComplaintRequests(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(complaintService.getAllUserComplaintsRequests(userDetails.getId()));
    }

    @Operation(
            summary = "Получить локализованные названия типов жалоб",
            description = "Возвращает список локализованных названий типов жалоб. Локализация зависит от языка, указанного в заголовке запроса (например, `Accept-Language`)."
    )
    @GetMapping("/local-complaint-types")
    public List<String> getLocalComplaintTypes() {
        Locale locale = LocaleContextHolder.getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        return Arrays.stream(ComplaintType.values())
                .map(type -> bundle.getString(type.name()))
                .toList();
    }

    @Operation(
            summary = "Получить типы жалоб с локализованными названиями",
            description = "Возвращает типы жалоб с их локализованными названиями. Локализация зависит от языка, указанного в заголовке запроса (например, `Accept-Language`)."
    )
    @GetMapping("/complaint-types")
    public ResponseEntity<Map<String, String>> getComplaintTypes() {
        return new ResponseEntity<>(complaintService.getComplaintTypes(), HttpStatus.OK);
    }

    @Operation(
            summary = "Изменить статус разрешения жалобы",
            description = "Изменяет статус разрешения жалобы на противоположный (с `решено` на `не решено` и наоборот). Доступ к этому методу ограничен: " +
                    "- Администраторы (`ADMIN`). " +
                    "- Модераторы (`MODERATOR`)."
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @PatchMapping("/{complaintId}")
    public ResponseEntity<HttpStatus> handleChangeComplaintResolvedStatus(@PathVariable("complaintId") int complaintId) {
        complaintService.changeComplaintResolvedStatus(complaintId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
