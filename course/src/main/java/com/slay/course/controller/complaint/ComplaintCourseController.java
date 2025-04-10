package com.slay.course.controller.complaint;

import com.slay.course.DTO.request.complaint.ComplaintCourseRequest;
import com.slay.course.DTO.response.complaint.ComplaintCourseDTO;
import com.slay.course.service.complaint.ComplaintCourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/complaint-course")
@AllArgsConstructor
@Tag(name = "Complaint course", description = "Жалоба на курс")
public class ComplaintCourseController {
    private final ComplaintCourseService complaintCourseService;

    @Operation(
            summary = "Создать жалобу на курс",
            description = "Создает новую жалобу на курс на основе переданных данных. Возвращает статус `201 CREATED` в случае успешного создания жалобы."
    )
    @PreAuthorize("isAuthenticated()")
    @PostMapping()
    public ResponseEntity<HttpStatus> handleCreateCourseComplaint(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid ComplaintCourseRequest complaint) {
        complaintCourseService.createCourseComplaint(userDetails, complaint);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Получить детали жалобы на курс по её идентификатору",
            description = "Возвращает детали жалобы на курс по её идентификатору. Доступ к этому методу ограничен:" +
        "- Администраторы (`ADMIN`)." +
        "- Модераторы (`MODERATOR`)." +
        "- Пользователь, который является отправителем жалобы."
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @GetMapping("/{complaintId}")
    public ResponseEntity<ComplaintCourseDTO> getComplaintCourseDetailByComplaint(@PathVariable int complaintId) {
        return new ResponseEntity<>(complaintCourseService.getComplaintCourseByComplaintId(complaintId), HttpStatus.OK);
    }

    @Operation(
            summary = "Получить типы жалоб на курс с локализованными названиями",
            description = "Возвращает список типов жалоб на курс с их локализованными названиями. Локализация зависит от языка, указанного в заголовке запроса (например, `Accept-Language`)."
    )
    @GetMapping("/types")
    public ResponseEntity<Map<String, String>> getComplaintCourseTypes() {
        return new ResponseEntity<>(complaintCourseService.getComplaintCourseTypes(), HttpStatus.OK);
    }


}
