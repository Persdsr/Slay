package com.slay.complaint.controller.complaint;

import com.slay.complaint.dto.request.complaint.ComplaintUserRequest;
import com.slay.complaint.dto.response.complaint.ComplaintUserDTO;
import com.slay.complaint.security.UserDetailsImpl;
import com.slay.complaint.service.complaint.ComplaintUserService;
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

import java.util.Set;

@RestController
@RequestMapping("/api/complaint-user")
@AllArgsConstructor
@Tag(name = "Complaint user", description = "Жалоба на пользователя")
public class ComplaintUserController {

    private final ComplaintUserService complaintUserService;

    @Operation(
            summary = "Получить жалобу на пользователя по её идентификатору",
            description = "Возвращает детали жалобы на пользователя по её идентификатору. Доступ к этому методу ограничен:" +
        "- Администраторы (`ADMIN`)." +
        "- Модераторы (`MODERATOR`)." +
        "- Пользователь, который является отправителем жалобы."
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @GetMapping("/{complaintId}")
    public ResponseEntity<ComplaintUserDTO> getComplaintUserByComplaint(@PathVariable int complaintId) {
        return new ResponseEntity<>(complaintUserService.getComplaintUserByComplaintId(complaintId), HttpStatus.OK);
    }

    @Operation(
            summary = "Создать жалобу на пользователя",
            description = "Создает новую жалобу на пользователя на основе переданных данных. Возвращает созданную жалобу."
    )
    @PreAuthorize("isAuthenticated()")
    @PostMapping()
    public ResponseEntity<HttpStatus> handleCreateUserComplaint(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid ComplaintUserRequest complaint) {
        complaintUserService.createUserComplaint(userDetails.getId(), complaint);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Получить список забаненных пользователей",
            description = "Возвращает список жалоб на пользователей, которые были забанены. Доступ к этому методу ограничен:" +
        "- Администраторы (`ADMIN`)." +
        "- Модераторы (`MODERATOR`)."
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @GetMapping("/banned-users")
    public ResponseEntity<Set<ComplaintUserDTO>> getBannedUsers() {
        return ResponseEntity.ok().body(complaintUserService.getBannedUsers());
    }

}
